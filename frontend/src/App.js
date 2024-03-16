import './App.scss';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import HomePage from './pages/HomePage/HomePage';
import AuthPage from './pages/AuthPage/AuthPage';
import NotFound from './pages/NotFound/NotFound'
import Header from './containers/Header/Header';
import routes from './utils/routes.json'
import Market from './pages/Market/Market';
import Leaderboard from './pages/Leaderboard/Leaderboard';
import About from './pages/About/About';
import Stock from './pages/Stock/Stock';
import {useEffect, useState} from "react";
import {Auth} from "./utils/Auth";


function App() {

    const [isAuth, setIsAuth] = useState(false);
    const auth = new Auth();

    const signOut = () => {
        auth.signOut()
        setIsAuth(false)
    }

    useEffect(() => {
        setIsAuth(auth.isLoggedIn())
    }, []);

    return (
        <div className="App">
            <Router>
                <Header isAuth={isAuth} handleClick={signOut}/>
                <Routes>
                    <Route path={routes.home} element={<HomePage isAuth={isAuth}/>}/>
                    <Route path={routes.auth} element={<AuthPage setIsAuth={setIsAuth}/>}/>
                    <Route path={routes.market} element={<Market/>}/>
                    <Route path={routes.leaderboard} element={<Leaderboard/>}/>
                    <Route path={routes.about} element={<About/>}/>
                    <Route path={routes.stock} element={<Stock/>}/>
                    <Route path="*" element={<NotFound/>}/>
                </Routes>
            </Router>
        </div>
    );
}

export default App;
