package com.awesome.view

import com.vaadin.flow.component.Text
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route


@Route
class MainView : VerticalLayout() {
    init {
        add(Text("Welcome to MainView."))
    }
}