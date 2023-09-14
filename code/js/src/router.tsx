import React, { Children, useEffect, useState} from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider, Route, createBrowserRouter, Link, useParams } from 'react-router-dom';
import { useFetch, extractLinks } from './utils';
import { LinkModel } from './models/link';

const router = createBrowserRouter(
    [
        { 
            path: "/",
            element: <Home />
        },
        {
            path: "/ranking", element: <Ranking />
        },
        {
            path: "/about", element: <About />
        },
        {
            path: "/user/:id", element: <UserHome />
        },
        {
            path: "/game", element: <GamesHome />
        },
        {
            path: "/game/start", element: <StartGame />
        },
        {
            path: "/game/active", element: <ActiveGames />
        },
        {
            path: "/game/finished", element: <FinishedGames />
        },
        {
            path: "/game/:id", element: <Game />
        },
        {
            path: "/game/:id/setup", element: <GameSetup />
        },
        {
            path: "/game/:id/board/:who", element: <GameBoard />
        },
        {
            path: "/game/:id/shoot", element: <GameShoot />
        },
        {
            path: "/logIn", element: <LogIn />
        },
        {
            path: "/signUp", element: <SignUp />
        },
        {
            path: "*", element: <NotFound />
        }//TODO use errorElement.
    ]
)

//Use token to check if user is logged in
function Home(){
    //const [token, setToken] = useState<string | null>(null);
    const [links, setLinks] = useState(Array<LinkModel>());
    
    useEffect(() => {
        const home = useFetch("localhost:8080/");
        setLinks(extractLinks(home[0]));
        }, [links]
    );

    return(
        <div>
            <p>Welcome to DAW's BattleShip Web Game</p>
            <p>Here you can play a game of BattleShip with your friends</p>
            <p>Click on the links below to navigate through the site</p>
            <ul>
                {links.map((link: LinkModel) => <li><Link to={link.url}>{link.rel}</Link></li>)}
            </ul>
        </div>       
    )//TODO verify output
        
}


function About(){
    
    const [aboutInfo, setAboutInfo] = useState<string | null>(null);

    useEffect(() => {
        const about = useFetch("localhost:8080/about");
        setAboutInfo(about[0]);
        }, [aboutInfo]
    );

    return(
        <div>
            <h1>About</h1>
            <p>Here you can find information about the app and it's developers</p>
            <p>{aboutInfo}</p>
        </div>
    )
}

function Ranking(){

    const [ranking, setRanking] = useState<Array<string>>([]);

    useEffect(() => {
        const rank = useFetch("localhost:8080/ranking");
        setRanking(rank);
        }, [ranking]
    );

    return(
        <div>
            <h1>Ranking</h1>
            <p>Here you can see the ranking of the best players</p>
            <ul>
                {ranking.map((user: string) => <li>{user}</li>)}
            </ul>
        </div>
    )
}

function UserHome(){
    let { id } = useParams();

    const [user, setUser] = useState<string | null>(null);

    useEffect(() => {
        const user = useFetch("localhost:8080/user/" + id);
        setUser(user[0]);
        }, [user]
    );

    return(
        <div>
            <h1>UserHome</h1>
            <p>{user}</p>
            <Link to="/user/:id/profilepicture">
                <button>Edit Picture</button>
            </Link>
        </div>
    )
}

function GamesHome(){
    //TODO very precarious way to get the id, we should display two buttons to start, see active and see finished games
    return(
        <div>
            <h1>GamesHome</h1>
            <Link to="/game/start">
                <button>Start</button>    
            </Link>	
            <Link to="/game/active">
                <button>ActiveGames</button>
            </Link>
            <Link to="/game/finished">
                <button>FinishedGames</button>	
            </Link>
            <input type={'text'}>id to search</input>
            <Link to="/game/:id">
                <button>Submit</button>
            </Link>
        </div>
    )
}

function StartGame(){
    return(
        <div>
            TODO start a game
        </div>
    )
}

function ActiveGames(){
    return(
        <div>
            TODO display active games
        </div>
    )
}

function FinishedGames(){
    return(
        <div>
            TODO display finished games
        </div>
    )
}

function Game(){
    //TODO make only available access according to state of the game
    let { id } = useParams();

    //TODO maybe put this in a separate component
    const [links, setLinks] = useState(Array<LinkModel>());

    useEffect(() => {
        const game = useFetch("localhost:8080/game/" + id);
        setLinks(extractLinks(game[0]));
        }, [links]
    );
    return(
        <div>
            <h1>Game Details</h1>
            <ul>
                {links.map((link: LinkModel) => <li><Link to={link.url}>{link.rel}</Link></li>)}
            </ul>
        </div>
    )
}

function GameSetup(){
    //TODO make a proper game setup
    return(
        <div>
            <h1>GameSetup</h1>
        </div>
    )
}

function GameBoard(){
    //TODO make a proper game board
    return(
        <div>
            <h1>GameBoard</h1>
        </div>
    )
}

function GameShoot(){
    //TODO make a proper game shoot
    return(
        <div>
            <h1>GameShoot</h1>
        </div>
    )
}

function LogIn(){
    //TODO make a proper login page
    return(
        <div>
            <h1>LogIn</h1>
        </div>
    )
}

function SignUp(){
    //TODO make a proper signup page
    return(
        <div>
            <h1>SignUp</h1>
        </div>
    )
}

function NotFound(){
    //TODO make a proper 404 page
    return(
        <div>
            <h1>NotFound</h1>
            <p>Sorry, the page you are looking for does not exist</p>
        </div>
    )
}

export function demo(){
    const root = createRoot(document.getElementById("container"));
    root.render(<RouterProvider router={router}/>)
}