package com.documentmanager.service.mapper;

import com.documentmanager.domain.DocColNameStore;
import com.documentmanager.domain.DocColValueStore;
import com.documentmanager.domain.DocStore;
import com.documentmanager.service.dto.DocColNameStoreDTO;
import com.documentmanager.service.dto.DocColValueStoreDTO;
import com.documentmanager.service.dto.DocStoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocColValueStore} and its DTO {@link DocColValueStoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocColValueStoreMapper extends EntityMapper<DocColValueStoreDTO, DocColValueStore> {
    @Mapping(target = "docStore", source = "docStore", qualifiedByName = "docStoreId")
    @Mapping(target = "docColNameStore", source = "docColNameStore", qualifiedByName = "docColNameStoreId")
    DocColValueStoreDTO toDto(DocColValueStore s);

    @Named("docStoreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fileName", source = "fileName")
    DocStoreDTO toDtoDocStoreId(DocStore docStore);

    @Named("docColNameStoreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "colName", source = "colName")
    DocColNameStoreDTO toDtoDocColNameStoreId(DocColNameStore docColNameStore);
}
