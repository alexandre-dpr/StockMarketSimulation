import React, {useEffect} from 'react';
import { Line } from 'react-chartjs-2';
import 'chart.js/auto';

function AnimatedLineChart() {


// Génération des données
    const data = [];
    const data2 = [];
    let prev = 100;
    let prev2 = 80;
    for (let i = 0; i < 100; i++) {
        prev += 5 - Math.random() * 10;
        data.push({ x: i, y: prev });
        prev2 += 5 - Math.random() * 10;
        data2.push({ x: i, y: prev2 });
    }

// Configuration de l'animation
    const totalDuration = 2000;
    const delayBetweenPoints = totalDuration / data.length;
    const previousY = (ctx) =>
        ctx.index === 0
            ? ctx.chart.scales.y.getPixelForValue(100)
            : ctx.chart.getDatasetMeta(ctx.datasetIndex).data[ctx.index - 1].getProps(['y'], true).y;

    const animation = {
        x: {
            type: 'number',
            easing: 'linear',
            duration: delayBetweenPoints,
            from: NaN,
            delay(ctx) {
                if (ctx.type !== 'data' || ctx.xStarted) {
                    return 0;
                }
                ctx.xStarted = true;
                return ctx.index * delayBetweenPoints;
            },
        },
        y: {
            type: 'number',
            easing: 'linear',
            duration: delayBetweenPoints,
            from: previousY,
            delay(ctx) {
                if (ctx.type !== 'data' || ctx.yStarted) {
                    return 0;
                }
                ctx.yStarted = true;
                return ctx.index * delayBetweenPoints;
            },
        },
    }
    const config = {
        data: {
            datasets: [
                {
                    borderColor: 'rgb(255, 99, 132,0.90)',
                    borderWidth: 3,
                    radius: 0,
                    data: data,
                },
                {
                    borderColor: 'rgba(54,162,235,0.90)',
                    borderWidth: 3,
                    radius: 0,
                    data: data2,
                },
            ],
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation,
            interaction: {
                intersect: false,
            },
            plugins: {
                legend: false,
                tooltip: {
                    enabled: false,
                },
            },
            scales: {
                x: {
                    type: 'linear',
                    display: true,
                    grid: {
                        display: false,
                    },
                    ticks: {
                        color: 'rgb(141,141,141)',
                    }
                },
                y: {
                    display: true,
                    grid: {
                        display: true,
                        color: 'rgba(141,141,141,0.17)',
                    },
                    ticks: {
                        color: 'rgb(141,141,141)',
                    }
                },
            },
        },
    };

    return <Line {...config} />;
}

export default AnimatedLineChart;
