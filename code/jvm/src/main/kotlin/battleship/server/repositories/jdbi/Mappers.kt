package battleship.server.repositories.jdbi

import battleship.server.domain.PasswordValidationInfo
import battleship.server.domain.TokenValidationInfo
import battleship.server.domain.UserID
import battleship.server.domain.game.Rules
import battleship.server.domain.game.board.Board
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import org.postgresql.util.PGobject
import java.sql.ResultSet
import java.sql.SQLException

class UserIDMapper : ColumnMapper<UserID> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): UserID {
        return UserID(r.getInt(columnNumber))
    }
}

class RulesMapper: ColumnMapper<Rules> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): Rules {
        val obj = r.getObject(columnNumber, PGobject::class.java)
        val value = obj.value
        checkNotNull(value)
        return JDBIGameRepository.deserializeRulesFromJson(value)
    }
}

class BoardMapper: ColumnMapper<BoardDto?> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): BoardDto? {
        val obj = r.getObject(columnNumber, PGobject::class.java).value ?: return null
        return JDBIGameRepository.deserializeBoardFromJson(obj)
    }
}

class PasswordValidationInfoMapper : ColumnMapper<PasswordValidationInfo> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): PasswordValidationInfo {
        return PasswordValidationInfo(r.getString(columnNumber))
    }
}

class TokenValidationInfoMapper : ColumnMapper<TokenValidationInfo> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): TokenValidationInfo {
        return TokenValidationInfo(r.getString(columnNumber))
    }
}

