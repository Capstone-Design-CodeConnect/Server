package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.domain.todo.Todo;
import CodeConnect.CodeConnect.dto.todo.*;
import CodeConnect.CodeConnect.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final ChatRoomService chatRoomService;
    private final TodoRepository todoRepository;

    // todo 생성
    public TodoResponseDto createTodo(CreateTodoRequestDto createTodoRequestDto) {

        ChatRoom chatRoom = chatRoomService.validateExistChatRoom(createTodoRequestDto.getRoomId());

        Todo todo = new Todo(createTodoRequestDto, chatRoom);
        todoRepository.save(todo);

        log.info("************************* {}번 채팅방 To-Do 생성", createTodoRequestDto.getRoomId());

        return new TodoResponseDto(todo);

    }

    // todo 업데이트
    public TodoResponseDto updateTodo(UpdateTodoRequestDto updateTodoRequestDto) {

        Todo todo = validateExistTodo(updateTodoRequestDto.getTodoId());

        todo.updateTodo(updateTodoRequestDto);
        todoRepository.save(todo);

        log.info("************************* {}번 Todo가 업데이트 되었습니다.", todo.getTodoId());

        return new TodoResponseDto(todo);

    }

    // todo 삭제
    public DeleteTodoResponseDto deleteTodo(DeleteTodoRequestDto deleteTodoRequestDto) {

        Todo todo = validateExistTodo(deleteTodoRequestDto.getTodoId());


        try {
            todoRepository.delete(todo);
            log.info("************************* {}번 Todo가 삭제 되었습니다.", todo.getTodoId());
            return new DeleteTodoResponseDto(todo.getTodoId(), true);
        } catch (Exception e) {
            log.error("************************* {}번 Todo 삭제중 오류가 발생했습니다: {}", todo.getTodoId(), e.getMessage());
            return new DeleteTodoResponseDto(todo.getTodoId(), false);
        }

    }

    // todo 존재여부 확인
    public Todo validateExistTodo(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        return optionalTodo.orElseThrow(() -> new NoSuchElementException("존재하지 않는 Todo 입니다: " + id));
    }


}
