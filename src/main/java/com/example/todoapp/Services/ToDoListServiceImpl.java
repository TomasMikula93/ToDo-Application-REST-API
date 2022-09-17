package com.example.todoapp.Services;

import com.example.todoapp.Models.DTOs.TaskDTO;
import com.example.todoapp.Models.DTOs.ToDoListDTO;
import com.example.todoapp.Models.Enums.Priority;
import com.example.todoapp.Models.Task;
import com.example.todoapp.Models.ToDoList;
import com.example.todoapp.Repositories.TaskRepository;
import com.example.todoapp.Repositories.ToDoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ToDoListServiceImpl implements ToDoListService {

    private final ToDoListRepository toDoListRepository;
    private final TaskRepository taskRepository;

    @Override
    public boolean checkSizeOfList(long id) {
        return toDoListRepository.findById(id).getListOfTasks().size() == 0;
    }

    @Override
    public ToDoListDTO makeToDoListDTO(long id) {
        List<Task> sublist = toDoListRepository.findById(id).getListOfTasks();
        List<TaskDTO> list = new ArrayList<>();
        for (Task task : sublist) {
            list.add(new TaskDTO(task.getId(),
                    task.getName(),
                    task.getDescription(),
                    task.getPriority(),
                    task.isDone()));
        }
        return new ToDoListDTO(list);

    }

    @Override
    public boolean checkIfListExists(long idOfToDoList) {
        return toDoListRepository.findOptionalById(idOfToDoList).isPresent();
    }

    @Override
    public void addTaskToList(Task task, long idOfToDoList) {
        Task task1 = new Task(task.getName(), task.getDescription(), Priority.NORMAL.getPriority());
        ToDoList toDoList = toDoListRepository.findById(idOfToDoList);
        taskRepository.save(task1);
        task1.setToDoList(toDoList);
        toDoList.getListOfTasks().add(task1);
        taskRepository.save(task1);
        toDoListRepository.save(toDoList);
    }

    @Override
    public void markTaskAsDone(Task task) {
        Task changedTask = taskRepository.findById(task.getId());
        changedTask.setDone(true);
        taskRepository.save(changedTask);
    }

    @Override
    public void changeTaskName(Task task) {
        Task changedTask = taskRepository.findById(task.getId());
        changedTask.setName(task.getName());
        taskRepository.save(changedTask);
    }

    @Override
    public void changeTaskDescription(Task task) {
        Task changedTask = taskRepository.findById(task.getId());
        changedTask.setDescription(task.getDescription());
        taskRepository.save(changedTask);
    }

    @Override
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public void changeTaskPriority(Task task) {
        Task changedTask = taskRepository.findById(task.getId());

        switch (task.getPriority()) {
            case "low" -> changedTask.setPriority(Priority.LOW.getPriority());
            case "normal" -> changedTask.setPriority(Priority.NORMAL.getPriority());
            case "high" -> changedTask.setPriority(Priority.HIGH.getPriority());
        }
        taskRepository.save(changedTask);
    }

    @Override
    public ToDoListDTO filterTasksByPriority(String value, long idOfList) {
        List<Task> listOfTasks = toDoListRepository.findById(idOfList).getListOfTasks();
        List<Task> sublist;
        List<TaskDTO> result = new ArrayList<>();

        switch (value) {
            case "low" -> {
                sublist = listOfTasks.stream().filter(task -> Objects.equals(task.getPriority(), "low")).toList();
                for (Task task : sublist) {
                    result.add(new TaskDTO(
                            task.getId(),
                            task.getName(),
                            task.getDescription(),
                            task.getPriority(),
                            task.isDone()
                    ));
                }
            }
            case "normal" -> {
                sublist = listOfTasks.stream().filter(task -> Objects.equals(task.getPriority(), "normal")).toList();
                for (Task task : sublist) {
                    result.add(new TaskDTO(
                            task.getId(),
                            task.getName(),
                            task.getDescription(),
                            task.getPriority(),
                            task.isDone()
                    ));
                }
            }
            case "high" -> {
                sublist = listOfTasks.stream().filter(task -> Objects.equals(task.getPriority(), "high")).toList();
                for (Task task : sublist) {
                    result.add(new TaskDTO(
                            task.getId(),
                            task.getName(),
                            task.getDescription(),
                            task.getPriority(),
                            task.isDone()
                    ));
                }
            }
        }

        return new ToDoListDTO(result);
    }

    @Override
    public ToDoListDTO filterTasksByStatus(String value, long idOfList) {
        List<Task> listOfTasks = toDoListRepository.findById(idOfList).getListOfTasks();
        List<Task> sublist;
        List<TaskDTO> result = new ArrayList<>();

        switch (value) {
            case "done" -> {
                sublist = listOfTasks.stream().filter(task -> Objects.equals(task.isDone(), true)).toList();
                for (Task task : sublist) {
                    result.add(new TaskDTO(
                            task.getId(),
                            task.getName(),
                            task.getDescription(),
                            task.getPriority(),
                            task.isDone()
                    ));
                }
            }
            case "undone" -> {
                sublist = listOfTasks.stream().filter(task -> Objects.equals(task.isDone(), false)).toList();
                for (Task task : sublist) {
                    result.add(new TaskDTO(
                            task.getId(),
                            task.getName(),
                            task.getDescription(),
                            task.getPriority(),
                            task.isDone()
                    ));
                }
            }
        }
        return new ToDoListDTO(result);
    }


}
