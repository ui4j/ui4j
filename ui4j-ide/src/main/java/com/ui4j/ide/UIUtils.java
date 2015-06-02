package com.ui4j.ide;

import javax.swing.JLabel;
import javax.swing.text.View;

public class UIUtils {

    public static java.awt.Dimension getPreferredSize(String html,
            boolean width, int prefSize) {
        JLabel resizer = new JLabel();
        resizer.setText("<html>" + html + "</html>");

        View view = (View) resizer.getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey);

        view.setSize(width ? prefSize : 0, width ? 0 : prefSize);

        float w = view.getPreferredSpan(View.X_AXIS);
        float h = view.getPreferredSpan(View.Y_AXIS);

        return new java.awt.Dimension((int) Math.ceil(w), (int) Math.ceil(h));
    }
}
