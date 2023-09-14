package battleship.server.repositories

interface TransactionManager {
    fun <R> run(callback: (Transaction) -> R): R
}
