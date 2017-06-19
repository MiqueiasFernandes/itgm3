import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ModeloExclusivo } from './modelo-exclusivo.model';
import { ModeloExclusivoService } from './modelo-exclusivo.service';
@Injectable()
export class ModeloExclusivoPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private modeloExclusivoService: ModeloExclusivoService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.modeloExclusivoService.find(id).subscribe((modeloExclusivo) => {
                this.modeloExclusivoModalRef(component, modeloExclusivo);
            });
        } else {
            return this.modeloExclusivoModalRef(component, new ModeloExclusivo());
        }
    }

    modeloExclusivoModalRef(component: Component, modeloExclusivo: ModeloExclusivo): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.modeloExclusivo = modeloExclusivo;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
