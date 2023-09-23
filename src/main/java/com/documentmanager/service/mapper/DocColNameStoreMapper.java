package com.documentmanager.service.mapper;

import com.documentmanager.domain.DocColNameStore;
import com.documentmanager.domain.DocStore;
import com.documentmanager.service.dto.DocColNameStoreDTO;
import com.documentmanager.service.dto.DocStoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocColNameStore} and its DTO {@link DocColNameStoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocColNameStoreMapper extends EntityMapper<DocColNameStoreDTO, DocColNameStore> {
    @Mapping(target = "docStore", source = "docStore", qualifiedByName = "docStoreId")
    DocColNameStoreDTO toDto(DocColNameStore s);

    @Named("docStoreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocStoreDTO toDtoDocStoreId(DocStore docStore);
}
