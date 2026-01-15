package com.currenjin.apimocker.mock

import com.currenjin.apimocker.parser.PropertyInfo
import com.currenjin.apimocker.parser.SchemaInfo
import net.datafaker.Faker
import org.springframework.stereotype.Component

@Component
class MockDataGenerator {

    private val faker = Faker()

    fun generate(schema: SchemaInfo): Map<String, Any> {
        return schema.properties.associate { prop ->
            prop.name to generateValue(prop)
        }
    }

    fun generateList(schema: SchemaInfo, count: Int = 3): List<Map<String, Any>> {
        return (1..count).map { generate(schema) }
    }

    private fun generateValue(prop: PropertyInfo): Any {
        return when (prop.type) {
            "string" -> generateString(prop)
            "integer" -> faker.number().numberBetween(1, 1000)
            "number" -> faker.number().randomDouble(2, 1, 1000)
            "boolean" -> faker.bool().bool()
            else -> "unknown"
        }
    }

    private fun generateString(prop: PropertyInfo): String {
        return when (prop.format) {
            "email" -> faker.internet().emailAddress()
            "date" -> faker.date().birthday().toString()
            "date-time" -> faker.date().birthday().toInstant().toString()
            "uri", "url" -> faker.internet().url()
            "uuid" -> faker.internet().uuid()
            else -> when {
                prop.name.contains("name", ignoreCase = true) -> faker.name().fullName()
                prop.name.contains("email", ignoreCase = true) -> faker.internet().emailAddress()
                prop.name.contains("phone", ignoreCase = true) -> faker.phoneNumber().phoneNumber()
                prop.name.contains("address", ignoreCase = true) -> faker.address().fullAddress()
                else -> faker.lorem().word()
            }
        }
    }
}
