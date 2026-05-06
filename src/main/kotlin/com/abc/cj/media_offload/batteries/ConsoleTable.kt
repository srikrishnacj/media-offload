package com.abc.cj.media_offload.batteries

// https://github.com/Cozmeh/ConsoleTable4java/blob/main/ConsoleTable/ct4j.java
class ConsoleTable {
    private val headers = mutableListOf<String>()
    private val rows = mutableMapOf<Int, MutableList<String>>()
    private val column = LinkedHashMap<String, MutableList<String>>()
    private val spacing = mutableListOf<Int>()

    private var horSep: String = "-"
    private var verSep: String = "|"
    private val whitespace: String = " "
    private var cornerJoint: String = "+"
    private var blockMultipleHeaders: Boolean = false
    private var uppercaseHeader: Boolean = false

    private var index: Int = 0

    // ---------------------------------- User-usable functions ---------------------------

    fun setHorizontalSeparator(horizontalSeparator: Char) {
        horSep = horizontalSeparator.toString()
    }

    fun setVerticalSeparator(verticalSeparator: Char) {
        verSep = verticalSeparator.toString()
    }

    fun setCornerJoint(cornerJoint: Char) {
        this.cornerJoint = cornerJoint.toString()
    }

    fun setUppercaseHeaders(uppercaseHeader: Boolean) {
        this.uppercaseHeader = uppercaseHeader
    }

    @Throws(DuplicateHeaderException::class, MultipleHeaderException::class)
    fun setHeader(vararg header: String) {
        val duplicate = mutableMapOf<String, Int>()
        header.forEachIndexed { i, h ->
            if (duplicate.containsKey(h)) {
                throw DuplicateHeaderException("Header cannot contain duplicate values ie : $h")
            }
            duplicate[h] = i
            headers.add(i, h)
        }
        if (blockMultipleHeaders) {
            throw MultipleHeaderException("A table cannot contain multiple column headers")
        }
        blockMultipleHeaders = true
    }

    @Throws(ElementSizeException::class)
    fun addRow(vararg row: String) {
        val tempRow = row.toMutableList()
        if (headers.size != tempRow.size) {
            throw ElementSizeException(
                "Number of elements in Header and Row must be equal\nHeader: ${headers.size} Row: ${tempRow.size}"
            )
        }
        rows[index] = tempRow
        index++
    }

    @Throws(ElementSizeException::class)
    fun printTable() {
        row2column()
        getMaxColumnLength()
        tableLine()
        tableHeader()
        tableLine()
        tableData()
        tableLine()
        println()
    }

    // -------------------------------------- Private functions -------------------------

    private fun tableData() {
        for ((_, values) in rows) {
            for (i in values.indices) {
                print("$verSep ${values[i]}${whitespace.repeat(spacing[i] - (values[i].length + 1))}")
            }
            println(verSep)
        }
    }

    private fun tableHeader() {
        headers.forEachIndexed { i, heading ->
            val displayHeading = if (uppercaseHeader) heading.uppercase() else heading
            print("$verSep $displayHeading${whitespace.repeat(spacing[i] - (heading.length + 1))}")
        }
        println(verSep)
    }

    private fun tableLine() {
        for (i in headers.indices) {
            print("$cornerJoint${horSep.repeat(spacing[i])}")
        }
        println(cornerJoint)
    }

    private fun getMaxColumnLength() {
        for ((columnHeading, columnValues) in column) {
            var max = columnHeading.length
            for (colVal in columnValues) {
                if (colVal.length > max) max = colVal.length
            }
            spacing.add(max + 2)
        }
    }

    private fun row2column() {
        column.clear()
        headers.forEachIndexed { i, str ->
            val tempColumn = rows.values.map { it[i] }.toMutableList()
            column[str] = tempColumn
        }
    }
}

// ---------------------------------- Exceptions ---------------------------

class ElementSizeException(msg: String) : Exception(msg)
class MultipleHeaderException(msg: String) : Exception(msg)
class DuplicateHeaderException(msg: String) : Exception(msg)