package com.lcwd.user.service.payload;

import java.time.LocalDateTime;
import java.util.List;

import com.lcwd.user.service.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserApiResponse {
	private LocalDateTime  timestamp;
    private String message;
    private String exception;
    @Builder.Default
    private boolean success = false;
    //private HttpStatus status;
    private int pageNo;
	private int pageSize;
	private long totalRecords;
	private List<User> user;
    

}
