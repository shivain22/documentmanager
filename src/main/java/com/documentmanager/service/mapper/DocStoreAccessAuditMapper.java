package com.documentmanager.service.mapper;

import com.documentmanager.domain.DocStore;
import com.documentmanager.domain.DocStoreAccessAudit;
import com.documentmanager.domain.User;
import com.documentmanager.service.dto.DocStoreAccessAuditDTO;
import com.documentmanager.service.dto.DocStoreDTO;
import com.documentmanager.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocStoreAccessAudit} and its DTO {@link DocStoreAccessAuditDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocStoreAccessAuditMapper extends EntityMapper<DocStoreAccessAuditDTO, DocStoreAccessAudit> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "docStore", source = "docStore", qualifiedByName = "docStoreId")
    DocStoreAccessAuditDTO toDto(DocStoreAccessAudit s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("docStoreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocStoreDTO toDtoDocStoreId(DocStore docStore);
}
