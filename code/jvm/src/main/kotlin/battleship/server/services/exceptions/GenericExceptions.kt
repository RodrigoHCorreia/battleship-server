package battleship.server.services.exceptions

//GENERIC HTTP EXCEPTIONS
class NotFoundException(message: String?) : BattleshipException(message)
class BadRequestException(message: String?) : BattleshipException(message)
class ForbiddenException(message: String?) : BattleshipException(message)
class UnauthorizedException(message: String?) : BattleshipException(message)
class InternalErrorException(message: String?) : BattleshipException(message)
class BadGatewayException(message: String?) : BattleshipException(message)
class ConflictException(message: String?) : BattleshipException(message)
// the T0D0() function doesnt throw an exception but an error, so we create our own
class NotImplementedException(message: String?) : BattleshipException(message)