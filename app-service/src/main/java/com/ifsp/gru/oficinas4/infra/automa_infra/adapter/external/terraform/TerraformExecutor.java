package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.external.terraform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TerraformExecutor {


    private void runCommandStream(String[] command, File workDir) throws IOException, InterruptedException {
        String commandString = String.join(" ", command);
        log.info("🚀 Executando (Stream): {} em {}", commandString, workDir.getAbsolutePath());

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workDir);

        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Comando falhou: " + commandString + " (Exit Code: " + exitCode + ")");
        }
    }

    private String runCommandCapture(String[] command, File workDir) throws IOException, InterruptedException {
        String commandString = String.join(" ", command);
        log.debug("Executando (Capture): {} em {}", commandString, workDir.getAbsolutePath());

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workDir);
        pb.redirectErrorStream(true);

        Process process = pb.start();
        String result;

        // Lê a saída
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            result = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            log.error("Erro ao capturar comando: {}. Output: {}", commandString, result);
            throw new RuntimeException("Erro Terraform Output: " + result);
        }

        return result;
    }

    public void init(File workDir) throws IOException, InterruptedException {
        runCommandStream(new String[]{"terraform", "init", "-input=false"}, workDir);
    }

    public String apply(File workDir) throws IOException, InterruptedException {

        runCommandStream(new String[]{"terraform", "apply", "-auto-approve", "-input=false"}, workDir);
        return "Apply completado (Logs no console)";
    }

    public String outputJson(File workDir) throws IOException, InterruptedException {
        // Output é pequeno e precisamos do JSON -> Use Capture
        return runCommandCapture(new String[]{"terraform", "output", "-json"}, workDir);
    }
}