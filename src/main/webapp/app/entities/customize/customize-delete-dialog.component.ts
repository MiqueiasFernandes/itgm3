import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Customize } from './customize.model';
import { CustomizePopupService } from './customize-popup.service';
import { CustomizeService } from './customize.service';

@Component({
    selector: 'jhi-customize-delete-dialog',
    templateUrl: './customize-delete-dialog.component.html'
})
export class CustomizeDeleteDialogComponent {

    customize: Customize;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private customizeService: CustomizeService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['customize']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.customizeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'customizeListModification',
                content: 'Deleted an customize'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-customize-delete-popup',
    template: ''
})
export class CustomizeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private customizePopupService: CustomizePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.customizePopupService
                .open(CustomizeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
