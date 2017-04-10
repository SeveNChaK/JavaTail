import org.junit.Test

import org.junit.Assert.*
import java.io.File
import java.nio.charset.Charset


class test{

    private fun assertFileContent(nameOut: String, nameIn: String) {
        val fileExpected = File(nameOut)
        val fileOut = File(nameIn)
        val contentExpected = fileExpected.readLines()
        val contentOut = fileOut.readLines()
        assertEquals(contentOut, contentExpected)
    }

    @Test
    fun main() {
        //outputExpected1.txt c = 5 n = null
        var inputString = arrayOf<String>("-c", "5", "-n", "-1", "-o", "files/output.txt", "files/input1.txt", "files/input2.txt", "files/input3.txt")
        TailLauncher.main(inputString)
        assertFileContent("files/output.txt", "files/outputExpected1.txt")

        //outputExpected2.txt c = null n = 4
        inputString = arrayOf<String>("-c", "-1", "-n", "4", "-o", "files/output.txt", "files/input1.txt", "files/input2.txt", "files/input3.txt")
        TailLauncher.main(inputString)
        assertFileContent("files/output.txt", "files/outputExpected2.txt")

        //outputExpected2.txt c = null n = null
        inputString = arrayOf<String>("-c", "-1", "-n", "-1", "-o", "files/output.txt", "files/input1.txt", "files/input2.txt", "files/input3.txt")
        TailLauncher.main(inputString)
        assertFileContent("files/output.txt", "files/outputExpected3.txt")
    }





}
