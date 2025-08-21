package com.currenjin.service

import com.currenjin.header.template.HeaderTemplate

interface HeaderTemplateFinder {
    fun findAll(): List<HeaderTemplate>
}
