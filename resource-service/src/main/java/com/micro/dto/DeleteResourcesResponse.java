package com.micro.dto;

import java.util.List;

public class DeleteResourcesResponse {
    private List<Long> ids;

    public DeleteResourcesResponse(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
