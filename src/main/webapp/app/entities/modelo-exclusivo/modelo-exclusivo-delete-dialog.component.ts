import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { ModeloExclusivo } from './modelo-exclusivo.model';
import { ModeloExclusivoPopupService } from './modelo-exclusivo-popup.service';
import { ModeloExclusivoService } from './modelo-exclusivo.service';

@Component({
    selector: 'jhi-modelo-exclusivo-delete-dialog',
    templateUrl: './modelo-exclusivo-delete-dialog.component.html'
})
export class ModeloExclusivoDeleteDialogComponent {

    modeloExclusivo: ModeloExclusivo;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private modeloExclusivoService: ModeloExclusivoService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['modeloExclusivo']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.modeloExclusivoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'modeloExclusivoListModification',
                content: 'Deleted an modeloExclusivo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-modelo-exclusivo-delete-popup',
    template: ''
})
export class ModeloExclusivoDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private modeloExclusivoPopupService: ModeloExclusivoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.modeloExclusivoPopupService
                .open(ModeloExclusivoDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
