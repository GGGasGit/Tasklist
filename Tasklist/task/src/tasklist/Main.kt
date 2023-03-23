package tasklist

import kotlin.system.exitProcess
import kotlinx.datetime.*
import java.lang.IllegalArgumentException
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

class Task {
    var priority = "C"
    var taskYear = 2023
    var taskMonth = 1
    var taskDay = 1
    var taskHour = 0
    var taskMinute = 0
    var description = mutableListOf<String>()

    fun askPriority() {
        while (true) {
            println("Input the task priority (C, H, N, L):")
            when (val input = readln().uppercase()) {
                "C", "H", "N", "L" -> {
                    priority = input.uppercase()
                    break
                }
                else -> continue
            }
        }
    }

    fun askDate() {
        while (true) {
            println("Input the date (yyyy-mm-dd):")
            try {
                val input = readln()
                if (Regex("\\d{4}-\\d{1,2}-\\d{1,2}").matches(input)) {
                    val (year, month, day) = input.split("-").map { it.toInt() }
                    LocalDate(year, month, day)
                    taskYear = year
                    taskMonth = month
                    taskDay = day
                    break
                } else {
                    println("The input date is invalid")
                    continue
                }
            } catch (e: IllegalArgumentException) {
                println("The input date is invalid")
                continue
            }
        }
    }

    fun askTime() {
        while (true) {
            println("Input the time (hh:mm):")
            try {
                val input = readln()
                if (Regex("\\d{1,2}:\\d{1,2}").matches(input)) {
                    val (hour, minute) = input.split(":").map { it.toInt() }
                    LocalDateTime(taskYear, taskMonth, taskDay, hour, minute)
                    taskHour = hour
                    taskMinute = minute
                    break
                } else {
                    println("The input time is invalid")
                    continue
                }
            } catch (e: IllegalArgumentException) {
                println("The input time is invalid")
                continue
            }
        }
    }

    fun askDescription(): Boolean {
        println("Input a new task (enter a blank line to end):")

        description.clear()
        while (true) {
            val input = readln().trim()
            if (input == "") {
                return if (description.isEmpty()) {
                    println("The task is blank")
                    false
                } else {
                    true
                }
            } else {
                description.add(input)
            }
        }
    }

}

class TaskList {
    val tasks = mutableListOf<Task>()

    fun addJsonTask(task: Task) {
        tasks.add(task)
    }

    fun addTask() {
        val task = Task()
        task.askPriority()
        task.askDate()
        task.askTime()
        if (task.askDescription()) tasks.add(task)
    }

    fun deleteTask() {
        if (tasks.isEmpty()) {
            return
        } else {
            while (true) {
                println("Input the task number (1-${tasks.size}):")
                val taskNumber = readln()
                if (Regex("\\d+").matches(taskNumber) && taskNumber.toInt() in 1..tasks.size) {
                    tasks.removeAt(taskNumber.toInt() - 1)
                    println("The task is deleted")
                    break
                } else {
                    println("Invalid task number")
                }
            }
        }
    }

    fun editTask() {
        if (tasks.isEmpty()) {
            return
        } else {
            while (true) {
                println("Input the task number (1-${tasks.size}):")
                val taskNumber = readln()
                if (Regex("\\d+").matches(taskNumber) && taskNumber.toInt() in 1..tasks.size) {
                    while (true) {
                        println("Input a field to edit (priority, date, time, task):")
                        when (readln()) {
                            "priority" -> {
                                tasks[taskNumber.toInt() - 1].askPriority()
                                println("The task is changed")
                                break
                            }
                            "date" -> {
                                tasks[taskNumber.toInt() - 1].askDate()
                                println("The task is changed")
                                break
                            }
                            "time" -> {
                                tasks[taskNumber.toInt() - 1].askTime()
                                println("The task is changed")
                                break
                            }
                            "task" -> {
                                if (tasks[taskNumber.toInt() - 1].askDescription()) println("The task is changed")
                                break
                            }
                            else -> println("Invalid field")
                        }
                    }
                    break
                } else {
                    println("Invalid task number")
                }
            }
        }
    }

    private fun convertDescription(description: String): String {
        var result: String
        return if (description.length <= 44) {
            result = description
            repeat(44 - description.length) {
                result += " "
            }
            "$result|"
        } else {
            description.substring(0, 44) +
                    "|\n|    |            |       |   |   |" +
                    convertDescription(description.substring(44))
        }
    }

    override fun toString(): String {
        return if (tasks.size == 0) {
            "No tasks have been input"
        } else {
            println("+----+------------+-------+---+---+--------------------------------------------+\n" +
                    "| N  |    Date    | Time  | P | D |                   Task                     |\n" +
                    "+----+------------+-------+---+---+--------------------------------------------+")
            val result = mutableListOf<String>()
            for (i in 0..tasks.lastIndex) {
                val taskDate = LocalDate(tasks[i].taskYear, tasks[i].taskMonth, tasks[i].taskDay)
                val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
                val numberOfDays = currentDate.daysUntil(taskDate)
                val dueTag = when {
                    numberOfDays > 0 -> "\u001B[102m \u001B[0m"
                    numberOfDays < 0 -> "\u001B[101m \u001B[0m"
                    else -> "\u001B[103m \u001B[0m"
                }
                val priorityTag = when (tasks[i].priority) {
                    "C" -> "\u001B[101m \u001B[0m"
                    "H" -> "\u001B[103m \u001B[0m"
                    "N" -> "\u001B[102m \u001B[0m"
                    "L" -> "\u001B[104m \u001B[0m"
                    else -> tasks[i].priority
                }
                val taskDescriptions = tasks[i].description.map { convertDescription(it) }
                val (date, time) = LocalDateTime(tasks[i].taskYear, tasks[i].taskMonth, tasks[i].taskDay,
                    tasks[i].taskHour, tasks[i].taskMinute).toString().replace("T", " ").split(" ")
                if (i < 9) {
                    result.add("| ${i + 1}  | $date | $time | $priorityTag | $dueTag |" +
                            "${taskDescriptions.joinToString("\n|    |            |       |   |   |")}\n" +
                            "+----+------------+-------+---+---+--------------------------------------------+")
                } else {
                    result.add("| ${i + 1} | $date | $time | $priorityTag | $dueTag |" +
                            "${taskDescriptions.joinToString("\n|    |            |       |   |   |")}\n" +
                            "+----+------------+-------+---+---+--------------------------------------------+")
                }
            }
            result.joinToString("\n") + "\n"
        }
    }
}

fun main() {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val type = Types.newParameterizedType(MutableList::class.java, Task::class.java)
    val taskListAdapter = moshi.adapter<MutableList<Task>>(type)

    val jsonFile = File("tasklist.json")
    val taskList = TaskList()

    if (jsonFile.exists()) {
        val jsonText = jsonFile.readText()
        val taskListFromFile = taskListAdapter.fromJson(jsonText)
        if (taskListFromFile != null) {
            for (item in taskListFromFile) {
                val task = Task()
                task.priority = item.priority
                task.taskDay = item.taskDay
                task.taskHour = item.taskHour
                task.taskMinute = item.taskMinute
                task.taskMonth = item.taskMonth
                task.taskYear = item.taskYear
                for (text in item.description) {
                    task.description.add(text)
                }
                taskList.addJsonTask(task)
            }
        }
    }

    while (true) {
        println("Input an action (add, print, edit, delete, end):")

        when (readln().lowercase()) {
            "end" -> {
                println("Tasklist exiting!")
                if (taskList.tasks.isNotEmpty()) {
                    val taskListJson = taskListAdapter.toJson(taskList.tasks)
                    File("tasklist.json").writeText(taskListJson)
                }
                exitProcess(0)
            }
            "add" -> taskList.addTask()
            "print" -> println(taskList)
            "edit" -> {
                println(taskList)
                taskList.editTask()
            }
            "delete" -> {
                println(taskList)
                taskList.deleteTask()
            }
            else -> println("The input action is invalid")
        }
    }

}