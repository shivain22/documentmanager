package com.documentmanager.service.mapper;

import com.documentmanager.domain.DocStore;
import com.documentmanager.domain.User;
import com.documentmanager.service.dto.DocStoreDTO;
import com.documentmanager.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocStore} and its DTO {@link DocStoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocStoreMapper extends EntityMapper<DocStoreDTO, DocStore> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    DocStoreDTO toDto(DocStore s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
