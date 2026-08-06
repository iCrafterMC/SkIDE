package com.skide.gui.crumbbar;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrumBarSkin<T> extends SkinBase<CrumBar<T>> {

    private static final String STYLE_CLASS_FIRST = "first";
    private final EventHandler<TreeModificationEvent<Object>> treeChildrenModifiedHandler =
            args -> updateBreadCrumbs();
    private final ChangeListener<TreeItem<T>> selectedPathChangeListener =
            (obs, oldItem, newItem) -> updateSelectedPath(newItem, oldItem);

    public CrumBarSkin(final CrumBar<T> control) {
        super(control);
        control.selectedCrumbProperty().addListener(selectedPathChangeListener);
        updateSelectedPath(getSkinnable().selectedCrumbProperty().get(), null);
    }

    private void updateSelectedPath(TreeItem<T> newTarget, TreeItem<T> oldTarget) {
        if (oldTarget != null) {
            oldTarget.removeEventHandler(
                    TreeItem.childrenModificationEvent(), treeChildrenModifiedHandler);
        }
        if (newTarget != null) {
            newTarget.addEventHandler(TreeItem.childrenModificationEvent(), treeChildrenModifiedHandler);
        }
        updateBreadCrumbs();
    }

    private void updateBreadCrumbs() {
        final CrumBar<T> buttonBar = getSkinnable();
        final TreeItem<T> pathTarget = buttonBar.getSelectedCrumb();
        final Callback<TreeItem<T>, Button> factory = buttonBar.getCrumbFactory();

        getChildren().clear();

        if (pathTarget != null) {
            List<TreeItem<T>> crumbs = constructFlatPath(pathTarget);

            for (int i = 0; i < crumbs.size(); i++) {
                Button crumb = createCrumb(factory, crumbs.get(i));
                crumb.setMnemonicParsing(false);
                if (i == 0) {
                    if (!crumb.getStyleClass().contains(STYLE_CLASS_FIRST)) {
                        crumb.getStyleClass().add(STYLE_CLASS_FIRST);
                    }
                } else {
                    crumb.getStyleClass().remove(STYLE_CLASS_FIRST);
                }

                getChildren().add(crumb);
            }
        }
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        for (int i = 0; i < getChildren().size(); i++) {
            Node n = getChildren().get(i);

            double nw = n.prefWidth(h);
            double nh = n.prefHeight(-1);

            if (i > 0) {
                double ins = n instanceof BreadCrumbButton ? ((BreadCrumbButton) n).getArrowWidth() : 0;
                x = x - ins;
            }

            n.resize(nw, nh);
            n.relocate(x, y);
            x += nw;
        }
    }

    private List<TreeItem<T>> constructFlatPath(TreeItem<T> bottomMost) {
        List<TreeItem<T>> path = new ArrayList<>();
        TreeItem<T> current = bottomMost;
        do {
            path.add(current);
            current = current.getParent();
        } while (current != null);
        Collections.reverse(path);
        return path;
    }

    private Button createCrumb(
            final Callback<TreeItem<T>, Button> factory,
            final TreeItem<T> selectedCrumb) {
        Button crumb = factory.call(selectedCrumb);
        crumb.getStyleClass().add("crumb");
        crumb.setOnAction(ae -> onBreadCrumbAction(selectedCrumb));
        return crumb;
    }

    protected void onBreadCrumbAction(final TreeItem<T> crumbModel) {
        final CrumBar<T> breadCrumbBar = getSkinnable();
        Event.fireEvent(breadCrumbBar, new CrumBar.BreadCrumbActionEvent<>(crumbModel));
        if (breadCrumbBar.isAutoNavigationEnabled()) {
            breadCrumbBar.setSelectedCrumb(crumbModel);
        }
    }

    public static class BreadCrumbButton extends Button {
        private final ObjectProperty<Boolean> first = new SimpleObjectProperty<>(this, "first");
        private final double arrowWidth = 5;
        private final double arrowHeight = 20;

        public BreadCrumbButton(String text) {
            this(text, null);
        }

        public BreadCrumbButton(String text, Node gfx) {
            super(text, gfx);
            first.set(false);
            getStyleClass().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable arg0) {
                    updateShape();
                }
            });
            updateShape();
        }

        private void updateShape() {
            this.setShape(createButtonShape());
        }

        public double getArrowWidth() {
            return arrowWidth;
        }

        private Path createButtonShape() {
            Path path = new Path();
            MoveTo e1 = new MoveTo(0, 0);
            path.getElements().add(e1);
            HLineTo e2 = new HLineTo();
            e2.xProperty().bind(this.widthProperty().subtract(arrowWidth));
            path.getElements().add(e2);
            LineTo e3 = new LineTo();
            e3.xProperty().bind(e2.xProperty().add(arrowWidth));
            e3.setY(arrowHeight / 2.0);
            path.getElements().add(e3);
            LineTo e4 = new LineTo();
            e4.xProperty().bind(e2.xProperty());
            e4.setY(arrowHeight);
            path.getElements().add(e4);
            HLineTo e5 = new HLineTo(0);
            path.getElements().add(e5);
            if (!getStyleClass().contains(STYLE_CLASS_FIRST)) {
                LineTo e6 = new LineTo(arrowWidth, arrowHeight / 2.0);
                path.getElements().add(e6);
            } else {
                ArcTo arcTo = new ArcTo();
                arcTo.setSweepFlag(true);
                arcTo.setX(0);
                arcTo.setY(0);
                arcTo.setRadiusX(15.0f);
                arcTo.setRadiusY(15.0f);
                path.getElements().add(arcTo);
            }
            ClosePath e7 = new ClosePath();
            path.getElements().add(e7);
            path.setFill(Color.BLACK);
            return path;
        }
    }
}
