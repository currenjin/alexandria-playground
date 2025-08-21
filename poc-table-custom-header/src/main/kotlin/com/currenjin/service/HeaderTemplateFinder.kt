package com.currenjin.service

import com.currenjin.domain.header.template.HeaderTemplate

interface HeaderTemplateFinder {
    fun findAll(): List<HeaderTemplate>
}
