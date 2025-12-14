package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.external.terraform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ProvisioningPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TerraformDeploymentService implements ProvisioningPort {

    private final TerraformContentGenerator contentGenerator;
    private final TerraformDirectoryManager directoryManager;
    private final TerraformExecutor executor;
    private final ObjectMapper objectMapper;
    @Override
    public ProvisioningResult provision(Application application, List<List<String>> resourceSnippets) {

        if (application.getId() == null) {
            log.error("Tentativa de provisionamento com Application ID nulo.");
            throw new BusinessException("A Aplicação deve ser salva e possuir um ID antes do deploy.");
        }

        log.info("Iniciando provisionamento Terraform para a aplicação ID: {}", application.getId());

        try {

            File workDir = directoryManager.getOrCreateWorkDir(application.getId());


            String cloudConfigYaml = contentGenerator.generateCloudConfigYaml(application, resourceSnippets);
            String mainTfContent = contentGenerator.generateMainTfContent(application, cloudConfigYaml);

            directoryManager.writeMainTfFile(workDir, mainTfContent);

            executor.init(workDir);
            String applyLog = executor.apply(workDir);

            String outputJson = executor.outputJson(workDir);

            String publicIp = extractPublicIpFromOutput(outputJson);

            log.info("Provisionamento concluído. IP: {}", publicIp);


            return new ProvisioningResult(publicIp, applyLog);

        } catch (Exception e) {
            log.error("Erro fatal durante o provisionamento Terraform para {}: {}", application.getName(), e.getMessage(), e);
            throw new ProvisioningFailureException("Falha ao executar provisionamento Terraform. Detalhes: " + e.getMessage(), e);
        }
    }


    private String extractPublicIpFromOutput(String outputJson) {
        try {

            var rootNode = objectMapper.readTree(outputJson);

            var ipAddressesNode = rootNode
                    .path("vm_ip_addresses")
                    .path("value");

            if (ipAddressesNode.isArray()) {
                for (var outerArray : ipAddressesNode) {
                    if (outerArray.isArray()) {
                        for (var innerElement : outerArray) {
                            String ip = innerElement.asText();

                            if (ip != null && !ip.startsWith("127.")) {
                                return ip;
                            }
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            log.error("Falha ao analisar o output JSON do Terraform: {}", e.getMessage(), e);
            return null;
        }
    }

    public static class ProvisioningFailureException extends RuntimeException {
        public ProvisioningFailureException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}