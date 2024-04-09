import React from 'react';
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import TableContainer from "@mui/material/TableContainer";
import './LeaderboardTable.scss.css'
import gold from "../../../assets/img/icons8-médaille-d'or-olympique-48.png"
import silver from "../../../assets/img/icons8-médaille-d'argent-olympique-48.png"
import bronze from "../../../assets/img/icons8-médaille-de-bronze-olympique-48.png"
import {round} from "../../../utils/services";

const LeaderboardTable = ({data}) => {
    return (
        <TableContainer component={Paper}>
            <Table sx={{minWidth: 650}} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>Rank</TableCell>
                        <TableCell>Nom utilisateur</TableCell>
                        <TableCell>Portefeuille</TableCell>
                        <TableCell>Pourcentage</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {data.leaderboard.slice(0, 15).map((row, index) => (
                        <TableRow>
                            <TableCell component="th" scope="row">
                                {
                                    row.rank === 1 ? <img style={{width: "30px",transform:"translateX(-10px)"}} src={gold} alt="medal"/>
                                        : row.rank === 2 ? <img style={{width: "30px",transform:"translateX(-10px)"}} src={silver} alt="medal"/>
                                            : row.rank === 3 ? <img style={{width: "30px",transform:"translateX(-10px)"}} src={bronze} alt="medal"/>
                                                : row.rank
                                }
                            </TableCell>
                            <TableCell>{row.username}</TableCell>
                            <TableCell>{`${round(row.totalValue,2)} $`}</TableCell>
                            <TableCell>{row.percentage}</TableCell>

                        </TableRow>
                    ))}
                    {data.user && data.user.rank > 15 &&
                        <>
                            <TableRow>
                                <TableCell colSpan={4} className={"alone-cell"}>.....</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>{data.user.rank}</TableCell>
                                <TableCell>{data.user.username}</TableCell>
                                <TableCell>{round(data.user.totalValue,2)}</TableCell>
                                <TableCell>{data.user.percentage}</TableCell>
                            </TableRow>
                        </>
                    }


                </TableBody>
            </Table>
        </TableContainer>
    )
        ;
};

export default LeaderboardTable;