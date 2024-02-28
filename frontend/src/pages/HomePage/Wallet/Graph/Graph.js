import React, { useEffect, useRef } from 'react';
import { Line } from "react-chartjs-2";

const Graph = () => {
    const chartRef = useRef(null);



    const chartData = {
        labels: ["1 Dec", "8 Dec", "16 Dec", "31 Dec"],
        datasets: [
            {
                data: [3, 2, 7, 4],
                borderColor: ["#03A9F5"],
            },
        ],
        legend: {
            display: false
        },
        tooltips: {
            enabled: false
        }
    };

    const options = {

        layout: {
            padding: {
                // Any unspecified dimensions are assumed to be 0
                left: 0
            }
        },
        maintainAspectRatio : false,
        plugins:{
            legend: {
                display: false
            },
        },
        tooltips: {
            enabled: false
        },
        scales: {

            x: {
                title: {
                    display: false,
                },
                grid: {
                    display: false,
                },
            },
            y: {
                title: {
                    display: false,
                },
                grid: {
                    display: false,
                }
            },
        }
    };

    return (
        <div className="h-100">
            <Line ref={chartRef} data={chartData} options={options} />
        </div>
    );
};

export default Graph;
