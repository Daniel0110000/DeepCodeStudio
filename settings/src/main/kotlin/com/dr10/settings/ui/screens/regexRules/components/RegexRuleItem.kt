package com.dr10.settings.ui.screens.regexRules.components

import com.dr10.common.models.RegexRuleModel
import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import org.jetbrains.skiko.Cursor
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

class RegexRuleItem(
    private val model: RegexRuleModel,
    private val listener: ItemListener<RegexRuleModel>?
): JPanel() {

    private val regexRuleItemLayout = GroupLayout(this)

    private lateinit var ruleIcon: JLabel
    private lateinit var ruleTitle: JLabel
    private lateinit var regex: JLabel
    private lateinit var deleteButton: JLabel

    init {
        layout = regexRuleItemLayout
        background = ThemeApp.awtColors.primaryColor
        isOpaque = true

        initializeComponents()
        setComponentsStructure()
    }

    private fun initializeComponents() {
        ruleIcon = JLabel(AppIcons.regexIcon)
        ruleTitle = createLabel(model.regexName)
        regex = createLabel(model.regexPattern)
        deleteButton = JLabel(AppIcons.closeTerminalIcon).apply {
            isVisible = true
            cursor = Cursor(java.awt.Cursor.HAND_CURSOR)
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    listener?.onItemDeleted(model)
                }
            })
        }
    }

    private fun setComponentsStructure() {
        regexRuleItemLayout.setHorizontalGroup(
            regexRuleItemLayout.createSequentialGroup()
                .addGap(10)
                .addComponent(ruleIcon)
                .addGap(5)
                .addGroup(
                    regexRuleItemLayout.createParallelGroup()
                        .addComponent(ruleTitle)
                        .addComponent(regex)
                )
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addComponent(deleteButton, 0, 25, 25)
                .addGap(10)
        )

        regexRuleItemLayout.setVerticalGroup(
            regexRuleItemLayout.createSequentialGroup()
                .addGap(7)
                .addGroup(
                    regexRuleItemLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(ruleIcon)
                        .addGroup(
                            regexRuleItemLayout.createSequentialGroup()
                                .addComponent(ruleTitle)
                                .addComponent(regex)
                        )
                        .addComponent(deleteButton, 0, 25, 25)
                )
                .addGap(7)
        )
    }

    private fun createLabel(text: String): JLabel = JLabel(text).apply {
        font = ThemeApp.text.fontInterRegular(11f)
        foreground = ThemeApp.colors.textColor.toAWTColor()
    }

    override fun paintComponent(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.color = background
        graphics.fillRoundRect(0, 0, width, height, 10, 10)
    }

}