package com.semicolon.backend.global.pageable;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {
    private List<E> dtoList; //실제 표시되는 dto 리스트
    private List<Integer> pageNumList; // 하단에 표시될 페이지 번호 리스트
    private PageRequestDTO pageRequestDTO; //
    private boolean prev, next;
    private int totalCnt, prevPage, nextPage, totalPage, current; //총 데이터수, 이전/다음페이지, 총페이지, 현재페이지

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCnt){
        this.dtoList=dtoList;
        this.pageRequestDTO=pageRequestDTO;
        this.totalCnt=(int)totalCnt;
        int end = (int)(Math.ceil(pageRequestDTO.getPage()/10.0))*10;
        int start = end - 9;
        int last = (int)(Math.ceil(totalCnt/(double)pageRequestDTO.getSize()));
        end = Math.min(end, last);
        this.prev = start>1;
        this.next = totalCnt > end * pageRequestDTO.getSize();
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().toList();
        if(prev) this.prevPage=start-1;
        if(next) this.nextPage=end+1;
        this.totalPage = last;
        this.current = pageRequestDTO.getPage();
    }

}
