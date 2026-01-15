package com.currenjin.apimocker.api

import com.currenjin.apimocker.generator.KotlinSpringGenerator
import com.currenjin.apimocker.generator.TypeScriptGenerator
import com.currenjin.apimocker.mock.MockDataGenerator
import com.currenjin.apimocker.parser.OpenApiParser
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = ["http://localhost:5173"])
class SpecController(
    private val parser: OpenApiParser,
    private val mockDataGenerator: MockDataGenerator,
    private val kotlinGenerator: KotlinSpringGenerator,
    private val typeScriptGenerator: TypeScriptGenerator
) {

    @PostMapping("/parse")
    fun parseSpec(@RequestBody request: ParseRequest): ParseResponse {
        val parsed = parser.parse(request.spec)
        return ParseResponse(
            endpoints = parsed.endpoints.map { EndpointDto(it.method, it.path, it.responseSchema) },
            schemas = parsed.schemas.map { SchemaDto(it.name, it.properties.map { p -> PropertyDto(p.name, p.type, p.format) }) }
        )
    }

    @PostMapping("/mock")
    fun generateMockData(@RequestBody request: MockRequest): Map<String, Any> {
        val parsed = parser.parse(request.spec)
        val schema = parsed.schemas.find { it.name == request.schemaName }
            ?: throw IllegalArgumentException("Schema not found: ${request.schemaName}")

        return if (request.asList) {
            mapOf("data" to mockDataGenerator.generateList(schema, request.count ?: 3))
        } else {
            mockDataGenerator.generate(schema)
        }
    }

    @PostMapping("/generate/kotlin")
    fun generateKotlinCode(@RequestBody request: ParseRequest): GeneratedCodeResponse {
        val parsed = parser.parse(request.spec)
        val controller = kotlinGenerator.generateController(parsed.endpoints)
        val dtos = parsed.schemas.map { kotlinGenerator.generateDto(it) }

        return GeneratedCodeResponse(
            files = listOf(
                GeneratedFile("ApiController.kt", controller)
            ) + dtos.mapIndexed { index, dto ->
                GeneratedFile("${parsed.schemas[index].name}.kt", dto)
            }
        )
    }

    @PostMapping("/generate/typescript")
    fun generateTypeScriptCode(@RequestBody request: ParseRequest): GeneratedCodeResponse {
        val parsed = parser.parse(request.spec)
        val interfaces = typeScriptGenerator.generateAllInterfaces(parsed.schemas)

        return GeneratedCodeResponse(
            files = listOf(GeneratedFile("types.ts", interfaces))
        )
    }
}

data class ParseRequest(val spec: String)
data class MockRequest(val spec: String, val schemaName: String, val asList: Boolean = false, val count: Int? = 3)

data class ParseResponse(val endpoints: List<EndpointDto>, val schemas: List<SchemaDto>)
data class EndpointDto(val method: String, val path: String, val responseSchema: String?)
data class SchemaDto(val name: String, val properties: List<PropertyDto>)
data class PropertyDto(val name: String, val type: String, val format: String?)

data class GeneratedCodeResponse(val files: List<GeneratedFile>)
data class GeneratedFile(val filename: String, val content: String)
