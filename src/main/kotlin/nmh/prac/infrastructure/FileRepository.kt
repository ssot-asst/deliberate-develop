package nmh.prac.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.util.*

abstract class FileRepository<T>(
    private val objectMapper: ObjectMapper,
    private val entityClass: Class<T>,
    private val fileName: String = entityClass.simpleName.removeSuffix("FileEntity")
        .lowercase(Locale.getDefault()) + "s.json"
) {
    protected val files: File = File(fileName)

    init {
        if (!files.exists()) {
            files.createNewFile()
            objectMapper.writeValue(files, emptyList<T>())
        }
    }

}