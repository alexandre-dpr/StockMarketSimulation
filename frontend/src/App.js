import './App.scss';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage/HomePage';
import AuthPage from './pages/AuthPage/AuthPage';
import NotFound from './pages/NotFound/NotFound'
import Header from './containers/Header/Header';
import routes from './utils/routes.json'
import Market from './pages/Market/Market';
import Leaderboard from './pages/Leaderboard/Leaderboard';
import About from './pages/About/About';
import Stock from './pages/Action/Stock';
import ProtectedRoute from "./components/ProtectedRoute/ProtectedRoute";
import AuthWrapper from "./components/ProtectedRoute/ProtectedRoute";
import {useEffect} from "react";

function App() {

  return (
    <div className="App">
      <Router>
        <Header />
        <Routes>
          <Route path={routes.home} element={<HomePage />} />
          <Route path={routes.auth} element={<AuthPage  />} />
          {
            /*
              <Route element={<AuthWrapper/>}>
            <Route path={routes.market} element={<Market />} />
          </Route>
             */
          }
          <Route path={routes.market} element={<Market />} />
          <Route path={routes.leaderboard} element={<Leaderboard />} />
          <Route path={routes.about} element={<About />} />
          <Route path={routes.stock} element={<Stock />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
