package com.awesome.view

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.setPrimary
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA


@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@CssImport.Container(value = [  // repeatable annotations are not supported by Kotlin, please vote for https://youtrack.jetbrains.com/issue/KT-12794
    CssImport("./styles/shared-styles.css"),
    CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
])
class MainView : KComposite() {
    private lateinit var nameField: TextField
    private lateinit var greetButton: Button

    // The main view UI definition
    private val root = ui {
        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        verticalLayout(classNames = "centered-content") {

            // Use TextField for standard text input
            nameField = textField("Your name")

            // Use Button for a clickable button
            greetButton = button("Say hello") {
                setPrimary(); addClickShortcut(Key.ENTER)
            }
        }
    }

    init {
        // attach functionality to the UI components.
        // It's a good practice to keep UI functionality separated from UI definition.

        // Button click listeners can be defined as lambda expressions
        greetButton.onLeftClick {
            Notification.show("Hello, ${nameField.value}")
        }
    }
}