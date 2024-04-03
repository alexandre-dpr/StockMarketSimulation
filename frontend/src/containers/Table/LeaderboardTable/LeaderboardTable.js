import React from 'react';
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import TableContainer from "@mui/material/TableContainer";
import './LeaderboardTable.scss.css'

const LeaderboardTable = ({data}) => {
    return (
        <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>Rank</TableCell>
                        <TableCell>Nom utilisateur</TableCell>
                        <TableCell>Portefeuille</TableCell>
                        <TableCell>Pourcentage</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {data.leaderboard.slice(0,15).map((row) => (
                        <TableRow
                            key={row.ticker}
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                            <TableCell component="th" scope="row">
                                {row.rank}
                            </TableCell>
                            <TableCell>{row.username}</TableCell>
                            <TableCell>{row.totalValue}</TableCell>
                            <TableCell>{row.percentage}</TableCell>

                        </TableRow>
                    ))}
                    {data.user.rank > 15 &&
                        <>
                            <TableRow>
                                <TableCell colSpan={4} className={"alone-cell"}>.....</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>{data.user.rank}</TableCell>
                                <TableCell>{data.user.username}</TableCell>
                                <TableCell>{data.user.totalValue}</TableCell>
                                <TableCell>{data.user.percentage}</TableCell>
                            </TableRow>
                        </>
                    }


                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default LeaderboardTable;