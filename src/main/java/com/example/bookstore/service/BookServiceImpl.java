package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {

        return bookRepository.findAll()
                .stream().map(bookMapper::toDto).toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id" + id));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto bookRequestDto) {
        Book bookUpdate = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("The book with this " + id + " doesn't exist")
        );
        bookUpdate.setTitle(bookRequestDto.getTitle());
        bookUpdate.setAuthor(bookRequestDto.getAuthor());
        bookUpdate.setIsbn(bookRequestDto.getIsbn());
        bookUpdate.setPrice(bookRequestDto.getPrice());
        bookUpdate.setDescription(bookRequestDto.getDescription());
        bookUpdate.setCoverImage(bookRequestDto.getCoverImage());
        return bookMapper.toDto(bookRepository.save(bookUpdate));
    }
}

