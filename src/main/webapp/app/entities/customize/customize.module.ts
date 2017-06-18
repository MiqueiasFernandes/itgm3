import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItgmSharedModule } from '../../shared';
import { ItgmAdminModule } from '../../admin/admin.module';
import {
    CustomizeService,
    CustomizePopupService,
    CustomizeComponent,
    CustomizeDetailComponent,
    CustomizeDialogComponent,
    CustomizePopupComponent,
    CustomizeDeletePopupComponent,
    CustomizeDeleteDialogComponent,
    customizeRoute,
    customizePopupRoute,
    CustomizeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...customizeRoute,
    ...customizePopupRoute,
];

@NgModule({
    imports: [
        ItgmSharedModule,
        ItgmAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CustomizeComponent,
        CustomizeDetailComponent,
        CustomizeDialogComponent,
        CustomizeDeleteDialogComponent,
        CustomizePopupComponent,
        CustomizeDeletePopupComponent,
    ],
    entryComponents: [
        CustomizeComponent,
        CustomizeDialogComponent,
        CustomizePopupComponent,
        CustomizeDeleteDialogComponent,
        CustomizeDeletePopupComponent,
    ],
    providers: [
        CustomizeService,
        CustomizePopupService,
        CustomizeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ItgmCustomizeModule {}
