/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.jmeterplugins.visualizers.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class FilterPanel extends JPanel {
    private static final long serialVersionUID = 240L;

    private JCheckBox jCheckBoxExclude;
    private JCheckBox jCheckBoxInclude;
    private JLabel jLabelExclude;
    private JLabel jLabelInclude;
    private JLabel jLabelRegExpExclude;
    private JLabel jLabelRegExpInclude;
    private JLabel jLabelStartEndOffset;
    private JTextField jTextFieldEndOffset;
    private JTextField jTextFieldExclude;
    private JTextField jTextFieldInclude;
    private JTextField jTextFieldStartOffset;

    public FilterPanel() {
        super();
        init();
    }

    private void init() {
        GridBagConstraints gridBagConstraints;

        jLabelInclude = new JLabel();
        jLabelExclude = new JLabel();
        jLabelStartEndOffset = new JLabel();
        jTextFieldStartOffset = new JTextField();
        jTextFieldEndOffset = new JTextField();
        jTextFieldInclude = new JTextField();
        jCheckBoxInclude = new JCheckBox();
        jTextFieldExclude = new JTextField();
        jCheckBoxExclude = new JCheckBox();
        jLabelRegExpInclude = new JLabel();
        jLabelRegExpExclude = new JLabel();

        this.setBorder(BorderFactory.createTitledBorder("Filter settings"));
        this.setLayout(new GridBagLayout());

        jLabelInclude.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelInclude.setText("Include labels");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        this.add(jLabelInclude, gridBagConstraints);

        jLabelExclude.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelExclude.setText("Exclude labels");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        this.add(jLabelExclude, gridBagConstraints);

        jLabelStartEndOffset.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelStartEndOffset.setText("Start / End offset (sec)");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        this.add(jLabelStartEndOffset, gridBagConstraints);

        jTextFieldStartOffset.setMinimumSize(new Dimension(30, 20));
        jTextFieldStartOffset.setPreferredSize(new Dimension(75, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        this.add(jTextFieldStartOffset, gridBagConstraints);

        jTextFieldEndOffset.setMinimumSize(new Dimension(30, 20));
        jTextFieldEndOffset.setPreferredSize(new Dimension(75, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(2, 0, 2, 2);
        this.add(jTextFieldEndOffset, gridBagConstraints);

        jTextFieldInclude.setMinimumSize(new Dimension(60, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(2, 0, 2, 0);
        this.add(jTextFieldInclude, gridBagConstraints);

        jCheckBoxInclude.setMargin(new Insets(0, 0, 0, 0));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        this.add(jCheckBoxInclude, gridBagConstraints);

        jTextFieldExclude.setMinimumSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(2, 0, 2, 0);
        this.add(jTextFieldExclude, gridBagConstraints);

        jCheckBoxExclude.setMargin(new Insets(0, 0, 0, 0));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        this.add(jCheckBoxExclude, gridBagConstraints);

        jLabelRegExpInclude.setText("RegExp");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        this.add(jLabelRegExpInclude, gridBagConstraints);

        jLabelRegExpExclude.setText("RegExp");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        this.add(jLabelRegExpExclude, gridBagConstraints);
    }

    public String getIncludeSampleLabels() {
        return jTextFieldInclude.getText();
    }

    public void setIncludeSampleLabels(String str) {
        jTextFieldInclude.setText(str);
    }

    public String getExcludeSampleLabels() {
        return jTextFieldExclude.getText();
    }

    public void setExcludeSampleLabels(String str) {
        jTextFieldExclude.setText(str);
    }

    public boolean isSelectedRegExpInc() {
        return jCheckBoxInclude.isSelected();
    }

    public void setSelectedRegExpInc(boolean b) {
        jCheckBoxInclude.setSelected(b);
    }

    public boolean isSelectedRegExpExc() {
        return jCheckBoxExclude.isSelected();
    }

    public void setSelectedRegExpExc(boolean b) {
        jCheckBoxExclude.setSelected(b);
    }

    public String getStartOffset() {
        return jTextFieldStartOffset.getText();
    }

    public void setStartOffset(long startOffset) {
        jTextFieldStartOffset.setText(String.valueOf(startOffset));
    }

    public String getEndOffset() {
        return jTextFieldEndOffset.getText();
    }

    public void setEndOffset(long endOffset) {
        jTextFieldEndOffset.setText(String.valueOf(endOffset));
    }

    public void clearGui() {
        jTextFieldInclude.setText("");
        jTextFieldExclude.setText("");
        jCheckBoxInclude.setSelected(false);
        jCheckBoxExclude.setSelected(false);
        jTextFieldStartOffset.setText("");
        jTextFieldEndOffset.setText("");
    }

}
