package ru.skypro.homework.service.impl;

import ru.skypro.homework.dto.CommentDto;
import java.util.Comparator;

public class CommentsComparator implements Comparator<CommentDto> {

    @Override
    public int compare(CommentDto o1, CommentDto o2) {

        return o1.getCreatedAt().compareTo(o2.getCreatedAt());
    }

}
