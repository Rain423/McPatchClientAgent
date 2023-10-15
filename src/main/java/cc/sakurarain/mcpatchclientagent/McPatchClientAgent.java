package cc.sakurarain.mcpatchclientagent;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.Buffer;
import java.util.Map;
import java.util.Random;

public class McPatchClientAgent {
    public static void main(String[] args) {
        premain(null, null);
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        try {
            int port = 0;
            for (int i = 0; i < 16; i++) {
                port = new Random().nextInt(20000) + 20000;
                if (Tools.checkPort(port)) {
                    break;
                }
                if (i == 16) {
                    return;
                }
            }

            File file = new File("agentConfig.yml");
            if (!file.exists()) {
                file.createNewFile();
                InputStream inputStream = McPatchClientAgent.class.getClassLoader().getResourceAsStream("agentConfig.yml");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }

                fileOutputStream.close();
            }

            Yaml yaml = new Yaml();
            Map<String, Object> param = yaml.load(new FileInputStream("./agentConfig.yml"));

            String configFile = (String) param.get("config_file");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(configFile)));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("server:")) {
                    line = "server: http://127.0.0.1:" + port;
                }
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            bufferedReader.close();

            FileWriter fileWriter = new FileWriter(new File(configFile));
            fileWriter.write(stringBuffer.toString());
            fileWriter.close();

            new Service(port, (int) param.get("thread"), (String) param.get("path")).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
