
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CalculadoraRestClient extends JFrame {
    private JTextField operador1TextField;
    private JTextField operador2TextField;
    private JComboBox<String> operacaoComboBox;
    private JTextField resultadoTextField;

    public CalculadoraRestClient() {
        super("Calculadora REST Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criação do painel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        add(panel);

        // Adição do layout do painel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(inputPanel, BorderLayout.NORTH);

        // Adição do campo do primeiro operador
        inputPanel.add(new JLabel("Operador 1:"));
        operador1TextField = new JTextField(10);
        inputPanel.add(operador1TextField);

        // Adição do campo do segundo operador
        inputPanel.add(new JLabel("Operador 2:"));
        operador2TextField = new JTextField(10);
        inputPanel.add(operador2TextField);

        // Adição da seleção da operação
        inputPanel.add(new JLabel("Operação:"));
        operacaoComboBox = new JComboBox<>(new String[] { "soma", "subtração", "multiplicação", "divisão" });
        inputPanel.add(operacaoComboBox);

        // Adição do botão Calcular
        JButton calcularButton = new JButton("Calcular");
        calcularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcular();
            }
        });
        inputPanel.add(calcularButton);

        // Adição do layout do resultado
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(outputPanel, BorderLayout.SOUTH);

        // Adição do resultado da operação
        outputPanel.add(new JLabel("Resultado da Operação:"));
        resultadoTextField = new JTextField(10);
        resultadoTextField.setEditable(false);
        outputPanel.add(resultadoTextField);

        // Adição de botão para consultar métodos disponiíveis na API
        JButton methodsBtn = new JButton("Ver métodos disponíveis");
        methodsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    URL url = new URL("https://calculadora-fxpc.onrender.com/operations"); // ajuste a URL conforme
                                                                                           // necessário
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine).append("\n");
                    }
                    in.close();

                    JTextArea textArea = new JTextArea(response.toString());
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 200));
                    JOptionPane.showMessageDialog(null, scrollPane, "Métodos disponíveis",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao consultar métodos: " + ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        inputPanel.add(methodsBtn);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void calcular() {
        // Variáveis para obter o operador1, operador2 e a operação
        String operador1 = operador1TextField.getText();
        String operador2 = operador2TextField.getText();
        String operacao = Normalizer.normalize(operacaoComboBox.getSelectedItem().toString(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("ç", "c"); // Remoção de ç e acentuação com o Normalizer

        // Criação da URL com os parâmetros de operação e operadores
        String urlStr = "https://calculadora-fxpc.onrender.com/operation/" + operacao
                + "/" + operador1 + "/"
                + operador2;

        try {
            // Conexão HTTP por meio do método POST com a URL criada
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String json = reader.lines().collect(Collectors.joining());
            reader.close();

            // Variável contendo o resultado da requisição HTTP
            String resultado = json.split("\"result\":")[1].split("}")[0];
            resultadoTextField.setText(resultado);
        } catch (MalformedURLException e) {
            System.out.println("URL inválida: " + urlStr);
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalculadoraRestClient();
            }
        });
    }
}
