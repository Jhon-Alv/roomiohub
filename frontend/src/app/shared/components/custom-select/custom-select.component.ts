import { Component, Input, forwardRef, ElementRef, HostListener, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

export interface SelectOption {
  value: any;
  label: string;
}

@Component({
  selector: 'app-custom-select',
  standalone: true,
  imports: [CommonModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CustomSelectComponent),
      multi: true
    }
  ],
  template: `
    <div class="relative w-full" [class.opacity-50]="disabled">
      <!-- Trigger button -->
      <button 
        type="button"
        [disabled]="disabled"
        (click)="toggleDropdown()"
        class="w-full flex items-center justify-between bg-surface border border-outline-variant rounded-lg text-on-surface focus:border-primary focus:ring-1 focus:ring-primary outline-none transition-all cursor-pointer disabled:cursor-not-allowed"
        [ngClass]="size === 'sm' ? 'px-3 py-2 font-body-sm text-body-sm' : 'px-4 py-2.5 font-body-md text-body-md'">
        <span class="truncate">{{ selectedLabel || placeholder }}</span>
        <span class="material-symbols-outlined text-outline transition-transform duration-200" [ngClass]="size === 'sm' ? 'text-[18px]' : 'text-[20px]'" [class.rotate-180]="isOpen()">expand_more</span>
      </button>

      <!-- Dropdown menu -->
      @if (isOpen()) {
        <div class="absolute z-50 w-full mt-1 bg-surface border border-outline-variant rounded-xl shadow-lg overflow-hidden animate-in fade-in zoom-in-95 duration-100 max-h-60 overflow-y-auto custom-scrollbar py-1">
          @for (option of options; track option.value) {
            <button 
              type="button"
              (click)="selectOption(option)"
              class="w-full text-left text-on-surface hover:bg-surface-container-low hover:text-primary transition-colors flex items-center justify-between"
              [ngClass]="size === 'sm' ? 'px-3 py-2 font-body-sm text-body-sm' : 'px-4 py-2.5 font-body-md text-body-md'"
              [class.bg-surface-container-low]="option.value === value"
              [class.text-primary]="option.value === value">
              <span class="truncate">{{ option.label }}</span>
              @if (option.value === value) {
                <span class="material-symbols-outlined text-[18px]">check</span>
              }
            </button>
          } @empty {
            <div class="px-4 py-3 text-sm text-on-surface-variant text-center">No hay opciones</div>
          }
        </div>
      }
    </div>
  `
})
export class CustomSelectComponent implements ControlValueAccessor {
  @Input() options: SelectOption[] = [];
  @Input() placeholder: string = 'Seleccionar...';
  @Input() size: 'sm' | 'md' = 'md';
  
  value: any = null;
  disabled: boolean = false;
  isOpen = signal(false);

  constructor(private elementRef: ElementRef) {}

  get selectedLabel(): string {
    const selected = this.options.find(opt => opt.value === this.value);
    return selected ? selected.label : '';
  }

  // --- Control Value Accessor Implementation ---
  onChange: any = () => {};
  onTouch: any = () => {};

  writeValue(val: any): void {
    this.value = val;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  // --- Component Logic ---
  toggleDropdown() {
    if (!this.disabled) {
      this.isOpen.set(!this.isOpen());
    }
  }

  selectOption(option: SelectOption) {
    this.value = option.value;
    this.onChange(this.value);
    this.onTouch();
    this.isOpen.set(false);
  }

  // Close dropdown when clicking outside
  @HostListener('document:click', ['$event'])
  onClickOutside(event: Event) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.isOpen.set(false);
    }
  }
}
