export const homeRelList = [
    {
        rel: "ranking"   
    },
    {
        rel: "about"
    },
    {
        rel: "logIn"
    },
    {
        rel: "signUp"
    },
    {
        rel: "game"
    },
    {
        rel: "user/:id"
    }
]

export const rankingRelList = [
    {
        rel: "user/:id"
    }
]

export const userHomeRelList = [
    {
        rel: "profile-picture"
    }
]

export const gamesHomeRelList = [
    {
        rel: ":id"
    },
    {
        rel: "start"
    },
    {
        rel: "finished"
    },
    {
        rel: "active"
    }
]

export const gameRelList = [
    {
        rel: "setup"
    },
    {
        rel: "board/:who/" // todo remove who
    },
    {
        rel: "shoot"
    }
]

export const shotResultRelList = [
    {
        rel: "game" // todo averiguate if this is needed
    }
]