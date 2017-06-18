import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Compartilhar } from './compartilhar.model';
import { CompartilharService } from './compartilhar.service';
@Injectable()
export class CompartilharPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private compartilharService: CompartilharService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.compartilharService.find(id).subscribe((compartilhar) => {
                this.compartilharModalRef(component, compartilhar);
            });
        } else {
            return this.compartilharModalRef(component, new Compartilhar());
        }
    }

    compartilharModalRef(component: Component, compartilhar: Compartilhar): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.compartilhar = compartilhar;
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
