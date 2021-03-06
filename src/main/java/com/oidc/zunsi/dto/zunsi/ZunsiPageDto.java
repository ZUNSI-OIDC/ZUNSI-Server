package com.oidc.zunsi.dto.zunsi;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ZunsiPageDto {
    private List<ZunsiListRowDto> zunsiListDto;
    private Boolean hasNext;
}
