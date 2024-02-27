import React, { useEffect, useRef } from 'react';
import { Line } from "react-chartjs-2";

const Graph = () => {
    const chartRef = useRef(null);

    useEffect(() => {
        const handleResize = () => {
            if (chartRef.current && chartRef.current.chartInstance) {
                chartRef.current.chartInstance.resize();
            }
        };

        window.addEventListener('resize', handleResize);

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

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
                    display: true,
                },
                grid: {
                    display: false
                },
            },
            y: {
                title: {
                    display: true,
                },
                grid: {
                    display: false
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
