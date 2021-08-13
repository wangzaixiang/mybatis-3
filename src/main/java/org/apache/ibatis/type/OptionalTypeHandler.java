/**
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.type;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class OptionalTypeHandler<E> extends BaseTypeHandler<Optional<E>> {

  TypeHandler<E> elementHandler;

  private static Optional<Boolean> optBoolean;
  private static Optional<String> optString;
  public static final Type Boolean = getType("optBoolean");
  public static final Type String = getType("optString");

  public OptionalTypeHandler(TypeHandler<E> elementHandler) {
    this.elementHandler = elementHandler;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Optional<E> parameter, JdbcType jdbcType) throws SQLException {
    if(parameter.isPresent() == false) {
      elementHandler.setParameter(ps, i, null, jdbcType);
    }
    else {
      elementHandler.setParameter(ps, i, parameter.get(), jdbcType);
    }
  }

  @Override
  public Optional<E> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    E value = elementHandler.getResult(rs, columnIndex);
    return Optional.ofNullable(value);
  }

  @Override
  public Optional<E> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return Optional.ofNullable( elementHandler.getResult(rs, columnName) );
  }

  @Override
  public Optional<E> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return Optional.ofNullable( elementHandler.getResult(cs, columnIndex) );
  }

  private static Type getType(String field){
    try {
      Field theField = OptionalTypeHandler.class.getDeclaredField(field);
      return theField.getGenericType();
    }
    catch(NoSuchFieldException ex){
      throw new RuntimeException("getType " + field);
    }
  }
}
