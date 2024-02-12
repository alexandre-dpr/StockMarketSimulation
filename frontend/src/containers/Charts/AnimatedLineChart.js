import React from 'react';
import { Line } from 'react-chartjs-2';
import 'chart.js/auto';

// Génération des données
const data = [];
const data2 = [];
let prev = 100;
let prev2 = 80;
for (let i = 0; i < 200; i++) {
    prev += 5 - Math.random() * 10;
    data.push({ x: i, y: prev });
    prev2 += 5 - Math.random() * 10;
    data2.push({ x: i, y: prev2 });
}

// Configuration de l'animation
const totalDuration = 8000;
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
};
const config = {
    data: {
        datasets: [
            {
                borderColor: 'rgb(255, 99, 132)',
                borderWidth: 3,
                radius: 0,
                data: data,
            },
            {
                borderColor: 'rgb(54, 162, 235)',
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
                enabled: false, // Désactive les tooltips
            },
        },
        scales: {
            x: {
                type: 'linear',
                display: false,
                grid: {
                    display: false,
                },
            },
            y: {
                display: false,
                grid: {
                    display: false,
                },
            },
        },
    },
};



function AnimatedLineChart() {
    return <Line {...config} />;
}

export default AnimatedLineChart;
