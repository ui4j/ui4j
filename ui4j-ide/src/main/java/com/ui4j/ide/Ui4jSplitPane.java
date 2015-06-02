package com.ui4j.ide;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * A JSplitPane that uses a 1 pixel thin visible divider, but a 9 pixel wide
 * transparent drag area.
 */
public class Ui4jSplitPane extends JSplitPane {

    private static final long serialVersionUID = -6677336999015631623L;

    /**
     * The size of the transparent drag area.
     */
    private int dividerDragSize = 9;

    /**
     * The offset of the transparent drag area relative to the visible divider
     * line. Positive offset moves the drag area left/top to the divider line.
     * If zero then the drag area is right/bottom of the divider line. Useful
     * values are in the range 0 to {@link #dividerDragSize}. Default is
     * centered.
     */
    private int dividerDragOffset = 4;

    public Ui4jSplitPane() {
        this(HORIZONTAL_SPLIT);
    }

    public Ui4jSplitPane(int orientation) {
        super(orientation);
        setContinuousLayout(true);
        setDividerSize(1);
    }

    public int getDividerDragSize() {
        return dividerDragSize;
    }

    public void setDividerDragSize(int dividerDragSize) {
        this.dividerDragSize = dividerDragSize;
        revalidate();
    }

    public int getDividerDragOffset() {
        return dividerDragOffset;
    }

    public void setDividerDragOffset(int dividerDragOffset) {
        this.dividerDragOffset = dividerDragOffset;
        revalidate();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void layout() {
        super.layout();

        // increase divider width or height
        BasicSplitPaneDivider divider = ((BasicSplitPaneUI) getUI())
                .getDivider();
        Rectangle bounds = divider.getBounds();
        if (orientation == HORIZONTAL_SPLIT) {
            bounds.x -= dividerDragOffset;
            bounds.width = dividerDragSize;
        } else {
            bounds.y -= dividerDragOffset;
            bounds.height = dividerDragSize;
        }
        divider.setBounds(bounds);
    }

    @Override
    public void updateUI() {
        setUI(new SplitPaneWithZeroSizeDividerUI());
        revalidate();
    }

    // ---- class SplitPaneWithZeroSizeDividerUI -------------------------------

    private class SplitPaneWithZeroSizeDividerUI extends BasicSplitPaneUI {
        @Override
        public BasicSplitPaneDivider createDefaultDivider() {
            return new ZeroSizeDivider(this);
        }
    }

    // ---- class ZeroSizeDivider ----------------------------------------------

    private class ZeroSizeDivider extends BasicSplitPaneDivider {

        private static final long serialVersionUID = 3316032183265108712L;

        public ZeroSizeDivider(BasicSplitPaneUI ui) {
            super(ui);
            super.setBorder(null);
            Color color = UIManager.getColor("Ui4jSplitPane.background");
            if (color != null) {
                setBackground(color);
            } else {
                setBackground(UIManager.getColor("controlShadow"));
            }
        }

        @Override
        public void setBorder(Border border) {
            // ignore
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(getBackground());
            if (orientation == HORIZONTAL_SPLIT)
                g.drawLine(dividerDragOffset, 0, dividerDragOffset,
                        getHeight() - 1);
            else
                g.drawLine(0, dividerDragOffset, getWidth() - 1,
                        dividerDragOffset);
        }

        @Override
        protected void dragDividerTo(int location) {
            super.dragDividerTo(location + dividerDragOffset);
        }

        @Override
        protected void finishDraggingTo(int location) {
            super.finishDraggingTo(location + dividerDragOffset);
        }
    }
}
