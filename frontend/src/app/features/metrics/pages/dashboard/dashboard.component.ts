import { Component, OnInit, OnDestroy, AfterViewInit, inject, signal, computed, viewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MetricsService } from '../../services/metrics.service';
import { SedeStateService } from '../../../../core/services/sede-state.service';
import {
  DashboardResponse, HabitacionCatalogoItem,
  HistorialEstadoResponse, PageResponse, DashboardEstadoResumen, IngresosDiariosResponse
} from '../../../../shared/models/metrics.models';
import { Chart, BarController, BarElement, CategoryScale, LinearScale, Tooltip } from 'chart.js';
import { CustomSelectComponent, SelectOption } from '../../../../shared/components/custom-select/custom-select.component';

Chart.register(BarController, BarElement, CategoryScale, LinearScale, Tooltip);

@Component({
  selector: 'app-metrics-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, CustomSelectComponent],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit, OnDestroy, AfterViewInit {
  private metricsService = inject(MetricsService);
  private sedeState = inject(SedeStateService);

  canvas = viewChild<ElementRef<HTMLCanvasElement>>('ingresosCanvas');

  dashboard = signal<DashboardResponse | null>(null);
  catalogo = signal<HabitacionCatalogoItem[]>([]);
  historialPage = signal<PageResponse<HistorialEstadoResponse> | null>(null);
  ingresosDiarios = signal<IngresosDiariosResponse | null>(null);

  selectedHabitacionId = signal<string | null>(null);
  pageSize = signal(5);
  currentPage = signal(0);
  ingresosDias = signal(7);

  sizeOptions = [5, 10, 25, 50, 100];

  sizeSelectOptions: SelectOption[] = this.sizeOptions.map(s => ({ value: s, label: s.toString() }));

  filterHabitacionOptions = computed<SelectOption[]>(() => [
    { value: '', label: 'Todas las habitaciones' },
    ...this.catalogo().map(h => ({ value: h.id, label: h.numero + ' - ' + h.categoria }))
  ]);

  private chart: Chart | null = null;

  selectedIngresosTotal = computed(() => this.ingresosDiarios()?.total ?? 0);

  ngOnInit() {
    this.cargarDashboard();
    this.cargarCatalogo();
    this.cargarHistorial();
  }

  ngAfterViewInit() {
    setTimeout(() => this.cargarIngresos(), 200);
  }

  ngOnDestroy() {
    if (this.chart) { this.chart.destroy(); this.chart = null; }
  }

  cargarDashboard() {
    const sedeId = this.sedeState.activeSede()?.id;
    if (!sedeId) return;
    this.metricsService.getDashboard(sedeId).subscribe({ next: data => this.dashboard.set(data) });
  }

  cargarCatalogo() {
    const sedeId = this.sedeState.activeSede()?.id;
    if (!sedeId) return;
    this.metricsService.getCatalogoHabitaciones(sedeId).subscribe({ next: data => this.catalogo.set(data) });
  }

  cargarHistorial() {
    const sedeId = this.sedeState.activeSede()?.id;
    if (!sedeId) return;
    this.metricsService.getHistorialEstados(sedeId, this.selectedHabitacionId() ?? undefined, this.currentPage(), this.pageSize()).subscribe({ next: data => this.historialPage.set(data) });
  }

  cargarIngresos() {
    const sedeId = this.sedeState.activeSede()?.id;
    if (!sedeId) return;
    this.metricsService.getIngresosDiarios(sedeId, this.ingresosDias()).subscribe({ next: data => { this.ingresosDiarios.set(data); setTimeout(() => this.renderChart(data), 0); } });
  }

  selectDias(dias: number) {
    this.ingresosDias.set(dias);
    this.cargarIngresos();
  }

  renderChart(data: IngresosDiariosResponse) {
    if (this.chart) { this.chart.destroy(); this.chart = null; }
    const el = this.canvas()?.nativeElement;
    if (!el) return;

    const days = this.ingresosDias();
    const labels = data.dias.map(d => {
      const f = new Date(d.fecha + 'T12:00:00');
      return f.toLocaleDateString('es-PE', { day: 'numeric', month: 'short' });
    });
    if (days === 30) {
      for (let i = 0; i < labels.length; i++) {
        if (i % 5 !== 0 && i !== labels.length - 1) labels[i] = '';
      }
    }

    this.chart = new Chart(el, {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          data: data.dias.map(d => d.monto),
          backgroundColor: data.dias.map((_, i) => i === data.dias.length - 1 ? '#6750A4' : '#E8DEF8'),
          borderRadius: days === 7 ? 6 : 3,
          borderSkipped: false,
          barPercentage: days === 7 ? 0.7 : 0.98,
          categoryPercentage: 0.9,
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: {
            backgroundColor: '#1D1B20',
            titleColor: '#E6E0E9',
            bodyColor: '#E6E0E9',
            cornerRadius: 6,
            padding: 8,
            titleFont: { size: 12 },
            bodyFont: { size: 14, weight: 'bold' },
            displayColors: false,
            callbacks: {
              title(items) {
                const idx = items[0].dataIndex;
                return new Date(data.dias[idx].fecha + 'T12:00:00').toLocaleDateString('es-PE', { weekday: 'short', day: 'numeric', month: 'short' });
              },
              label(item) { return `S/ ${Number(item.raw).toFixed(2)}`; }
            }
          },
        },
        scales: {
          x: { grid: { display: false }, border: { display: false }, ticks: { maxRotation: 0, font: { size: 10 }, color: '#938F99' } },
          y: { border: { display: false }, grid: { display: false }, ticks: { maxTicksLimit: 4, font: { size: 10 }, color: '#938F99', callback(v) { return Number(v) === 0 ? '0' : 'S/' + Number(v).toFixed(0); } } },
        },
      },
    });
  }

  changePageSize(size: number) { this.pageSize.set(size); this.currentPage.set(0); this.cargarHistorial(); }
  goToPage(page: number) { this.currentPage.set(page); this.cargarHistorial(); }
  filterByHabitacion(id: string | null) { this.selectedHabitacionId.set(id); this.currentPage.set(0); this.cargarHistorial(); }

  pages = computed(() => {
    const p = this.historialPage();
    return p ? Array.from({ length: p.totalPages }, (_, i) => i) : [];
  });

  formatEstado(e: string): string {
    switch (e) { case 'DISPONIBLE': return 'Disponible'; case 'OCUPADO': return 'Ocupado'; case 'SUCIO': return 'Sucio'; case 'EN_LIMPIEZA': return 'En Limpieza'; case 'MANTENIMIENTO': return 'Mantenimiento'; default: return e; }
  }

  formatFecha(f: string): string {
    const d = new Date(f); const hoy = new Date();
    const h = d.toLocaleTimeString('es-PE', { hour: '2-digit', minute: '2-digit' });
    return d.toDateString() === hoy.toDateString() ? `Hoy, ${h}` : d.toLocaleDateString('es-PE', { day: '2-digit', month: '2-digit' }) + `, ${h}`;
  }

  getBadgeClass(e: string): string {
    switch (e) { case 'DISPONIBLE': return 'bg-[#d1fae5] text-[#065f46]'; case 'OCUPADO': return 'bg-[#fee2e2] text-[#991b1b]'; case 'SUCIO': return 'bg-[#ffedd5] text-[#9a3412]'; case 'EN_LIMPIEZA': return 'bg-[#fef08a] text-[#854d0e]'; case 'MANTENIMIENTO': return 'bg-[#f1f5f9] text-[#334155] border border-[#cbd5e1]'; default: return 'bg-gray-100 text-gray-700'; }
  }

  getEstadoColor(e: string): string {
    switch (e) { case 'DISPONIBLE': return '#10b981'; case 'OCUPADO': return '#ef4444'; case 'SUCIO': return '#f97316'; case 'EN_LIMPIEZA': return '#eab308'; case 'MANTENIMIENTO': return '#64748b'; default: return '#94a3b8'; }
  }

  estadoPorcentaje(c: number, t: number): number { return t > 0 ? (c / t) * 100 : 0; }

  getDonutDash(est: DashboardEstadoResumen, all: DashboardEstadoResumen[], total: number): string {
    const circ = 2 * Math.PI * 40; const pct = this.estadoPorcentaje(est.cantidad, total) / 100; return `${circ * pct} ${circ}`;
  }

  getDonutOffset(i: number, all: DashboardEstadoResumen[], total: number): string {
    const circ = 2 * Math.PI * 40; let off = 0;
    for (let j = 0; j < i; j++) off += circ * (this.estadoPorcentaje(all[j].cantidad, total) / 100);
    return `${-off}`;
  }
}
