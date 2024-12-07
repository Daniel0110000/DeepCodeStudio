package com.dr10.settings.ui.screens.regexRules.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.settings.ui.screens.regexRules.RegexRuleModel
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

class RegexRuleItem(
    private val model: RegexRuleModel
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val regexRuleItemLayout = GroupLayout(this)
        layout = regexRuleItemLayout
        isOpaque = false

        val ruleIcon = JLabel(AppIcons.regexIcon)
        val ruleTitle = createLabel(model.regexName)
        val regex = createLabel(model.regex)

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