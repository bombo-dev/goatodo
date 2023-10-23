package com.goatodo.application.tag.request;

import lombok.Builder;

@Builder
public record TagCreateServiceRequest(
        String name
) {

}
