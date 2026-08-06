package com.skide.gui.crumbbar;

import javafx.beans.property.*;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

public class CrumBar<T> extends Control {

    private static final String DEFAULT_STYLE_CLASS = "bread-crumb-bar";

    private final Callback<TreeItem<T>, Button> defaultCrumbNodeFactory = crumb ->
            new CrumBarSkin.BreadCrumbButton(crumb.getValue() != null ? crumb.getValue().toString() : "");

    private final ObjectProperty<TreeItem<T>> selectedCrumb =
            new SimpleObjectProperty<>(this, "selectedCrumb");

    private final BooleanProperty autoNavigation =
            new SimpleBooleanProperty(this, "autoNavigationEnabled", true);

    private final ObjectProperty<Callback<TreeItem<T>, Button>> crumbFactory =
            new SimpleObjectProperty<>(this, "crumbFactory");

    private ObjectProperty<EventHandler<BreadCrumbActionEvent<T>>> onCrumbAction =
            new SimpleObjectProperty<>(this, "onCrumbAction");

    public CrumBar() {
        this(null);
    }

    public CrumBar(TreeItem<T> selectedCrumb) {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setSelectedCrumb(selectedCrumb);
        setCrumbFactory(defaultCrumbNodeFactory);
    }

    public static <T> TreeItem<T> buildTreeModel(@SuppressWarnings("unchecked") T... crumbs) {
        TreeItem<T> subRoot = null;
        for (T crumb : crumbs) {
            TreeItem<T> currentNode = new TreeItem<>(crumb);
            if (subRoot == null) {
                subRoot = currentNode;
            } else {
                subRoot.getChildren().add(currentNode);
                subRoot = currentNode;
            }
        }
        return subRoot;
    }

    public final ObjectProperty<TreeItem<T>> selectedCrumbProperty() {
        return selectedCrumb;
    }

    public final TreeItem<T> getSelectedCrumb() {
        return selectedCrumb.get();
    }

    public final void setSelectedCrumb(TreeItem<T> selectedCrumb) {
        this.selectedCrumb.set(selectedCrumb);
    }

    public final BooleanProperty autoNavigationEnabledProperty() {
        return autoNavigation;
    }

    public final boolean isAutoNavigationEnabled() {
        return autoNavigation.get();
    }

    public final void setAutoNavigationEnabled(boolean enabled) {
        autoNavigation.set(enabled);
    }

    public final ObjectProperty<Callback<TreeItem<T>, Button>> crumbFactoryProperty() {
        return crumbFactory;
    }

    public final Callback<TreeItem<T>, Button> getCrumbFactory() {
        return crumbFactory.get();
    }

    public final void setCrumbFactory(Callback<TreeItem<T>, Button> value) {
        if (value == null) {
            value = defaultCrumbNodeFactory;
        }
        crumbFactoryProperty().set(value);
    }

    public final ObjectProperty<EventHandler<BreadCrumbActionEvent<T>>> onCrumbActionProperty() {
        return onCrumbAction;
    }

    public final EventHandler<BreadCrumbActionEvent<T>> getOnCrumbAction() {
        return onCrumbActionProperty().get();
    }

    public final void setOnCrumbAction(EventHandler<BreadCrumbActionEvent<T>> value) {
        onCrumbActionProperty().set(value);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new CrumBarSkin<>(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getUserAgentStylesheet(CrumBar.class, "breadcrumbbar.css");
    }

    @SuppressWarnings("serial")
    public static class BreadCrumbActionEvent<TE> extends Event {

        @SuppressWarnings("rawtypes")
        public static final EventType<BreadCrumbActionEvent> CRUMB_ACTION = new EventType<>("CRUMB_ACTION");

        private final TreeItem<TE> selectedCrumb;

        public BreadCrumbActionEvent(TreeItem<TE> selectedCrumb) {
            super(CRUMB_ACTION);
            this.selectedCrumb = selectedCrumb;
        }

        public TreeItem<TE> getSelectedCrumb() {
            return selectedCrumb;
        }
    }
}
