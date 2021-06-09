package com.ilham.azurerosehealthmanagerapps.core.data.source.remote.response2

data class Response(
	val links: Links? = null,
	val hints: List<HintsItem?>? = null,
	val parsed: List<ParsedItem?>? = null,
	val text: String? = null
)

data class ServingSizesItem(
	val quantity: Double? = null,
	val label: String? = null,
	val uri: String? = null
)

data class Links(
	val next: Next? = null
)

data class QualifiedItem(
	val qualifiers: List<QualifiersItem?>? = null,
	val weight: Double? = null
)

data class MeasuresItem(
	val weight: Double? = null,
	val label: String? = null,
	val uri: String? = null
)

data class QualifiersItem(
	val label: String? = null,
	val uri: String? = null
)

data class Next(
	val href: String? = null,
	val title: String? = null
)

data class HintsItem(
	val measures: List<MeasuresItem?>? = null,
	val food: Food? = null
)

data class Nutrients(
	val pROCNT: Double? = null,
	val eNERCKCAL: Double? = null,
	val fAT: Double? = null,
	val cHOCDF: Double? = null,
	val fIBTG: Double? = null
)

data class Food(
	val image: String? = null,
	val foodId: String? = null,
	val categoryLabel: String? = null,
	val label: String? = null,
	val category: String? = null,
	val nutrients: Nutrients? = null
)

data class ParsedItem(
	val food: Food? = null
)

