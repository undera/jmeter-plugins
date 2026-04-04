package com.example.jmeter.importer;

import com.example.jmeter.importer.model.RequestModel;
import org.apache.jmeter.gui.action.AbstractAction;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JMeter GUI Action that opens the Swagger / Postman Importer dialog and
 * generates a JMeter Test Plan (.jmx) file.
 *
 * <p>Registered via the {@code org.apache.jmeter.gui.action.Command}
 * service-loader file so JMeter picks it up automatically and adds the
 * menu entry <b>Tools → Swagger / Postman Importer</b>.</p>
 */
public class SwaggerPostmanImporterAction extends AbstractAction {

    /** Command identifier registered with JMeter's ActionRouter. */
    public static final String COMMAND = "swagger_postman_importer";

    private static final Set<String> COMMANDS = new HashSet<>();
    static { COMMANDS.add(COMMAND); }

    @Override
    public Set<String> getActionNames() { return COMMANDS; }

    @Override
    public void doAction(ActionEvent e) {
        Frame parent = JOptionPane.getFrameForComponent((Component) e.getSource());
        new ImporterDialog(parent).setVisible(true);
    }

    // ========================================================================
    // Importer Dialog
    // ========================================================================

    public static class ImporterDialog extends JDialog {

        private static final int PAD = 8;

        private final JTextField   inputFileField  = new JTextField(42);
        private final JTextField   outputFileField = new JTextField(42);
        private final JTextField   planNameField   = new JTextField("Imported Test Plan", 42);
        private final JSpinner     threadSpinner   = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        private final JRadioButton swaggerRadio    = new JRadioButton("Swagger / OpenAPI  (.json / .yaml)", true);
        private final JRadioButton postmanRadio    = new JRadioButton("Postman Collection  (.json)");
        private final JTextArea    previewArea     = new JTextArea(12, 64);
        private final JLabel       statusLabel     = new JLabel(" ");

        public ImporterDialog(Frame parent) {
            super(parent, "Swagger / Postman Importer", true);
            buildUi();
            pack();
            setResizable(true);
            setLocationRelativeTo(parent);
        }

        // ----------------------------------------------------------------
        // UI construction
        // ----------------------------------------------------------------

        private void buildUi() {
            JPanel root = new JPanel(new BorderLayout(PAD, PAD));
            root.setBorder(BorderFactory.createEmptyBorder(PAD, PAD, PAD, PAD));

            // ── Form ────────────────────────────────────────────────────
            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets  = new Insets(4, 4, 4, 4);
            gc.anchor  = GridBagConstraints.WEST;
            gc.fill    = GridBagConstraints.HORIZONTAL;

            int row = 0;

            // Source type row
            gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
            form.add(new JLabel("Source type:"), gc);
            ButtonGroup bg = new ButtonGroup();
            bg.add(swaggerRadio); bg.add(postmanRadio);
            JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            radioPanel.add(swaggerRadio);
            radioPanel.add(Box.createHorizontalStrut(20));
            radioPanel.add(postmanRadio);
            gc.gridx = 1; gc.weightx = 1;
            form.add(radioPanel, gc);
            row++;

            // Input file row
            gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
            form.add(new JLabel("Input file:"), gc);
            gc.gridx = 1; gc.weightx = 1;
            form.add(buildBrowsePanel(inputFileField, false,
                    "Select Input File",
                    new FileNameExtensionFilter("Spec files (*.json, *.yaml, *.yml)", "json", "yaml", "yml")), gc);
            row++;

            // Output file row
            gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
            form.add(new JLabel("Output JMX:"), gc);
            gc.gridx = 1; gc.weightx = 1;
            form.add(buildBrowsePanel(outputFileField, true,
                    "Save JMX File",
                    new FileNameExtensionFilter("JMeter Test Plan (*.jmx)", "jmx")), gc);
            row++;

            // Plan name row
            gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
            form.add(new JLabel("Test Plan name:"), gc);
            gc.gridx = 1; gc.weightx = 1;
            form.add(planNameField, gc);
            row++;

            // Thread count row
            gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
            form.add(new JLabel("Thread count:"), gc);
            gc.gridx = 1; gc.weightx = 0;
            form.add(threadSpinner, gc);

            root.add(form, BorderLayout.NORTH);

            // ── Preview area ─────────────────────────────────────────────
            previewArea.setEditable(false);
            previewArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
            previewArea.setText("(Click Preview to see parsed requests before generating the JMX)");
            JScrollPane scroll = new JScrollPane(previewArea);
            scroll.setBorder(BorderFactory.createTitledBorder("Request Preview"));
            root.add(scroll, BorderLayout.CENTER);

            // ── Buttons ──────────────────────────────────────────────────
            JButton previewBtn  = new JButton("Preview");
            JButton generateBtn = new JButton("Generate JMX");
            JButton closeBtn    = new JButton("Close");

            previewBtn.addActionListener(ev  -> doPreview());
            generateBtn.addActionListener(ev -> doGenerate());
            closeBtn.addActionListener(ev    -> dispose());

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(statusLabel);
            btnPanel.add(Box.createHorizontalStrut(16));
            btnPanel.add(previewBtn);
            btnPanel.add(generateBtn);
            btnPanel.add(closeBtn);
            root.add(btnPanel, BorderLayout.SOUTH);

            setContentPane(root);
            getRootPane().setDefaultButton(generateBtn);
        }

        private JPanel buildBrowsePanel(JTextField field, boolean saveMode,
                                        String title, FileNameExtensionFilter filter) {
            JPanel p  = new JPanel(new BorderLayout(4, 0));
            JButton b = new JButton("Browse…");
            p.add(field, BorderLayout.CENTER);
            p.add(b,     BorderLayout.EAST);
            b.addActionListener(ev -> {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle(title);
                if (filter != null) { fc.setFileFilter(filter); fc.setAcceptAllFileFilterUsed(true); }
                int ret = saveMode ? fc.showSaveDialog(this) : fc.showOpenDialog(this);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    if (saveMode && !f.getName().toLowerCase().endsWith(".jmx"))
                        f = new File(f.getAbsolutePath() + ".jmx");
                    field.setText(f.getAbsolutePath());
                }
            });
            return p;
        }

        // ----------------------------------------------------------------
        // Actions
        // ----------------------------------------------------------------

        private List<RequestModel> parseInput() throws Exception {
            String inputPath = inputFileField.getText().trim();
            if (inputPath.isEmpty()) throw new IllegalArgumentException("Please select an input file.");
            return swaggerRadio.isSelected()
                    ? new SwaggerImporter().parse(inputPath)
                    : new PostmanImporter().parse(inputPath);
        }

        private void doPreview() {
            statusLabel.setText("Parsing…");
            statusLabel.setForeground(Color.DARK_GRAY);
            SwingUtilities.invokeLater(() -> {
                try {
                    List<RequestModel> requests = parseInput();
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format("Found %d request(s):%n%n", requests.size()));
                    for (int i = 0; i < requests.size(); i++) {
                        RequestModel r = requests.get(i);
                        sb.append(String.format("#%d  %s%n", i + 1, r.getName()));
                        sb.append(String.format("     %-8s %s://%s%s%s%n",
                                r.getMethod(), r.getProtocol(), r.getHost(),
                                r.getPort() > 0 ? ":" + r.getPort() : "", r.getPath()));
                        if (!r.getHeaders().isEmpty()) {
                            sb.append("     Headers:");
                            for (RequestModel.HeaderEntry h : r.getHeaders())
                                sb.append(String.format("%n       %s: %s", h.getName(), h.getValue()));
                            sb.append("\n");
                        }
                        if (!r.getQueryParams().isEmpty()) {
                            sb.append("     Query params:");
                            for (RequestModel.ParamEntry qp : r.getQueryParams())
                                sb.append(String.format("%n       %s=%s", qp.getName(), qp.getValue()));
                            sb.append("\n");
                        }
                        if (r.getBodyData() != null && !r.getBodyData().isEmpty()) {
                            String body = r.getBodyData().length() > 200
                                    ? r.getBodyData().substring(0, 197) + "…"
                                    : r.getBodyData();
                            sb.append(String.format("     Body: %s%n", body));
                        }
                        sb.append("\n");
                    }
                    previewArea.setText(sb.toString());
                    previewArea.setCaretPosition(0);
                    statusLabel.setText("Parsed " + requests.size() + " request(s).");
                    statusLabel.setForeground(new Color(0, 128, 0));
                } catch (Exception ex) {
                    previewArea.setText("Error: " + ex.getMessage());
                    statusLabel.setText("Parse failed.");
                    statusLabel.setForeground(Color.RED);
                }
            });
        }

        private void doGenerate() {
            statusLabel.setText("Generating…");
            statusLabel.setForeground(Color.DARK_GRAY);
            SwingUtilities.invokeLater(() -> {
                try {
                    String outputPath = outputFileField.getText().trim();
                    if (outputPath.isEmpty())
                        throw new IllegalArgumentException("Please select an output JMX file.");

                    List<RequestModel> requests = parseInput();
                    if (requests.isEmpty())
                        throw new IllegalStateException("No requests found in the selected file.");

                    String planName = planNameField.getText().trim();
                    if (planName.isEmpty()) planName = "Imported Test Plan";

                    int threads = (Integer) threadSpinner.getValue();
                    new JMeterTestPlanBuilder().writeJmx(requests, planName, threads, outputPath);

                    statusLabel.setText("JMX saved: " + outputPath);
                    statusLabel.setForeground(new Color(0, 128, 0));
                    JOptionPane.showMessageDialog(this,
                            "Test plan generated successfully!\n\n" + outputPath +
                            "\n\n" + requests.size() + " HTTP request(s) included.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    statusLabel.setText("Generation failed.");
                    statusLabel.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(this,
                            "Error: " + ex.getMessage(),
                            "Generation Failed", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
}
